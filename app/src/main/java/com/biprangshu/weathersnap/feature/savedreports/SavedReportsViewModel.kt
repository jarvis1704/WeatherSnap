package com.biprangshu.weathersnap.feature.savedreports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biprangshu.weathersnap.domain.model.Report
import com.biprangshu.weathersnap.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SavedReportsViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : ViewModel() {

    val reports: StateFlow<List<Report>> = repository.getAllReports()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
