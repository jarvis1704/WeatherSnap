package com.biprangshu.weathersnap.feature.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CameraUiState(
    val hasCameraPermission: Boolean = false,
    val isCapturing: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState

    fun onPermissionResult(granted: Boolean) {
        _uiState.update { it.copy(hasCameraPermission = granted) }
    }

    fun onCaptureStarted() {
        _uiState.update { it.copy(isCapturing = true, error = null) }
    }

    fun onCaptureFailed(message: String) {
        _uiState.update { it.copy(isCapturing = false, error = message) }
    }

    fun onCaptureCompleted() {
        _uiState.update { it.copy(isCapturing = false) }
    }
}
