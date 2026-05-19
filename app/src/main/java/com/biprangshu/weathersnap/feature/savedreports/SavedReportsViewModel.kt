package com.biprangshu.weathersnap.feature.savedreports

import androidx.lifecycle.ViewModel
import com.biprangshu.weathersnap.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedReportsViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : ViewModel()
