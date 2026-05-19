package com.biprangshu.weathersnap.feature.createreport

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.biprangshu.weathersnap.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateReportViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val repository: WeatherRepository,
) : ViewModel()
