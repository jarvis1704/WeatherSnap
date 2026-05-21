package com.biprangshu.weathersnap.feature.createreport

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biprangshu.weathersnap.domain.model.Report
import com.biprangshu.weathersnap.domain.model.Weather
import com.biprangshu.weathersnap.domain.repository.WeatherRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

data class CreateReportUiState(
    val weather: Weather? = null,
    val notes: String = "",
    val capturedImagePath: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null,
)

data class CompressionInfo(
    val imagePath: String = "",
    val originalSize: Long = 0L,
    val compressedSize: Long = 0L

)

@HiltViewModel
class CreateReportViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: WeatherRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReportUiState())
    val uiState: StateFlow<CreateReportUiState> = _uiState

    private val _compressionInfo = MutableStateFlow(CompressionInfo())

    val compressionInfo: StateFlow<CompressionInfo> = _compressionInfo

    init {
        val weatherJson = savedStateHandle.get<String>("weatherJson") ?: ""
        val weather = if (weatherJson.isNotBlank()) {
            try {
                val decoded = URLDecoder.decode(weatherJson, StandardCharsets.UTF_8.toString())
                Gson().fromJson(decoded, Weather::class.java)
            } catch (_: Exception) {
                Log.e("create_report", "failure in getting weather json from savedstate handle")
                null
            }
        } else null

        //developer's judgement challenge: using savedstatehandle to handle configuration changes or background process kill.
        val notes = savedStateHandle.get<String>("notes") ?: ""
        val capturedImagePath = savedStateHandle.get<String>("capturedImagePath")
        val compressedPath = savedStateHandle.get<String>("compressedPath") ?: ""
        val originalSize = savedStateHandle.get<Long>("originalSize") ?: 0L
        val compressedSize = savedStateHandle.get<Long>("compressedSize") ?: 0L

        _uiState.update { it.copy(weather = weather, notes = notes, capturedImagePath = capturedImagePath) }

        if (compressedPath.isNotBlank()) {
            _compressionInfo.update {
                it.copy(imagePath = compressedPath, originalSize = originalSize, compressedSize = compressedSize)
            }
        }
    }

    fun onNotesChanged(notes: String) {
        //on very keystroke, save notes to savedstatehandle
        savedStateHandle["notes"] = notes
        _uiState.update { it.copy(notes = notes) }
    }

    fun onImageCaptured(path: String) {
        //save path to savedstate handle to retain info after config change
        savedStateHandle["capturedImagePath"] = path
        _uiState.update { it.copy(capturedImagePath = path) }

        viewModelScope.launch {
            val (originalSize, compressedPath, compressedSize) = withContext(Dispatchers.IO) {
                compressImage(path)
            }

            //save path and size to savedstate handle to retain info after config change
            savedStateHandle["compressedPath"] = compressedPath
            savedStateHandle["originalSize"] = originalSize
            savedStateHandle["compressedSize"] = compressedSize

            _compressionInfo.update {
                it.copy(
                    imagePath = compressedPath,
                    originalSize = originalSize,
                    compressedSize = compressedSize
                )
            }
        }
    }

    fun onSaveReport() {
        val state = _uiState.value
        val weather = state.weather ?: return

        _uiState.update { it.copy(isSaving = true, error = null) }

        viewModelScope.launch {
            try {
                val report = Report(
                    weather = weather,
                    notes = state.notes,
                    imagePath = _compressionInfo.value.imagePath,
                    originalImageSizeBytes = _compressionInfo.value.originalSize,
                    compressedImageSizeBytes = _compressionInfo.value.compressedSize,
                    createdAt = System.currentTimeMillis(),
                )
                repository.saveReport(report)

                //deleteing raw photo as compressed image is saved to room
                withContext(Dispatchers.IO) {
                    state.capturedImagePath?.let { File(it).delete() }
                }
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        //if report not saved or user discarded, delete the temp files
        if (!_uiState.value.isSaved) {
            val rawPath = _uiState.value.capturedImagePath
            val compressedPath = _compressionInfo.value.imagePath
            rawPath?.let { File(it).delete() }
            if (compressedPath.isNotBlank()) File(compressedPath).delete()
        }
    }

    //function to compress image
    private fun compressImage(inputPath: String): Triple<Long, String, Long> {
        if (inputPath.isBlank()) return Triple(0L, "", 0L)

        val inputFile = File(inputPath)
        val originalSize = if (inputFile.exists()) inputFile.length() else 0L

        val bitmap = BitmapFactory.decodeFile(inputPath)
            ?: return Triple(originalSize, inputPath, originalSize)

        val outputFile = File(context.filesDir, "compressed_${System.currentTimeMillis()}.jpg")
        outputFile.outputStream().use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out)
        }
        bitmap.recycle()

        return Triple(originalSize, outputFile.absolutePath, outputFile.length())
    }
}
