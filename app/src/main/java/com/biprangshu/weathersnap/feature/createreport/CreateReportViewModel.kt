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

@HiltViewModel
class CreateReportViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: WeatherRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReportUiState())
    val uiState: StateFlow<CreateReportUiState> = _uiState

    init {
        //getting weather data from existing state handle
        val weatherJson = savedStateHandle.get<String>("weatherJson") ?: ""
        if (weatherJson.isNotBlank()) {
            try {
                val decoded = URLDecoder.decode(weatherJson, StandardCharsets.UTF_8.toString())
                val weather = Gson().fromJson(decoded, Weather::class.java)
                _uiState.update { it.copy(weather = weather) }
            } catch (_: Exception) {
                Log.e("create_report", "failure in getting weather json from savedstate handle")
            }
        }
    }

    fun onNotesChanged(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun onImageCaptured(path: String) {
        _uiState.update { it.copy(capturedImagePath = path) }
    }

    fun onSaveReport() {
        val state = _uiState.value
        val weather = state.weather ?: return
        val imagePath = state.capturedImagePath ?: ""

        _uiState.update { it.copy(isSaving = true, error = null) }

        viewModelScope.launch {
            try {
                val (originalSize, compressedPath, compressedSize) = withContext(Dispatchers.IO) {
                    compressImage(imagePath)
                }
                val report = Report(
                    weather = weather,
                    notes = state.notes,
                    imagePath = compressedPath,
                    originalImageSizeBytes = originalSize,
                    compressedImageSizeBytes = compressedSize,
                    createdAt = System.currentTimeMillis(),
                )
                repository.saveReport(report)
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }

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
