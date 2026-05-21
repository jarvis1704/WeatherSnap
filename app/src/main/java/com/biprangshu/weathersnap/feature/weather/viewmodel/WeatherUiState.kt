package com.biprangshu.weathersnap.feature.weather.viewmodel

import com.biprangshu.weathersnap.domain.model.City
import com.biprangshu.weathersnap.domain.model.Weather

sealed interface SuggestionsState {
    data object Idle : SuggestionsState
    data object Loading : SuggestionsState
    data class Success(val cities: List<City>) : SuggestionsState
    data class Error(val message: String) : SuggestionsState
}

sealed interface WeatherState {
    data object Empty : WeatherState
    data object Loading : WeatherState
    data class Success(val weather: Weather) : WeatherState
    data class Error(val message: String) : WeatherState
}

data class WeatherUiState(
    val searchQuery: String = "",
    val suggestionsState: SuggestionsState = SuggestionsState.Idle,
    val weatherState: WeatherState = WeatherState.Empty,
    val isSuggestionsDropdownVisible: Boolean = false,
)
