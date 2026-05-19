package com.biprangshu.weathersnap.feature.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biprangshu.weathersnap.domain.model.City
import com.biprangshu.weathersnap.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private var searchDebounceJob: Job? = null
    private var lastSelectedCity: City? = null

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchDebounceJob?.cancel()
        if (query.length < 2) {
            _uiState.update {
                it.copy(
                    suggestionsState = SuggestionsState.Idle,
                    isSuggestionsDropdownVisible = false,
                )
            }
            return
        }
        searchDebounceJob = viewModelScope.launch {
            delay(300)
            _uiState.update { it.copy(suggestionsState = SuggestionsState.Loading) }
            repository.searchCities(query)
                .onSuccess { cities ->
                    _uiState.update {
                        it.copy(
                            suggestionsState = SuggestionsState.Success(cities),
                            isSuggestionsDropdownVisible = cities.isNotEmpty(),
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            suggestionsState = SuggestionsState.Error(
                                error.message ?: "Failed to load suggestions",
                            ),
                            isSuggestionsDropdownVisible = false,
                        )
                    }
                }
        }
    }

    fun onCitySelected(city: City) {
        lastSelectedCity = city
        _uiState.update {
            it.copy(
                searchQuery = "${city.name}, ${city.country}",
                isSuggestionsDropdownVisible = false,
                suggestionsState = SuggestionsState.Idle,
                weatherState = WeatherState.Loading,
            )
        }
        viewModelScope.launch {
            repository.getWeather(city)
                .onSuccess { weather ->
                    _uiState.update { it.copy(weatherState = WeatherState.Success(weather)) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            weatherState = WeatherState.Error(
                                error.message ?: "Failed to load weather",
                            ),
                        )
                    }
                }
        }
    }

    fun onSuggestionsDismissed() {
        _uiState.update { it.copy(isSuggestionsDropdownVisible = false) }
    }

    fun onRetry() {
        val city = lastSelectedCity ?: return
        onCitySelected(city)
    }
}
