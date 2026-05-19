package com.biprangshu.weathersnap.feature.weather

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.biprangshu.weathersnap.feature.weather.components.EmptyStateCard
import com.biprangshu.weathersnap.feature.weather.components.SearchCard
import com.biprangshu.weathersnap.feature.weather.components.SuggestionsCard
import com.biprangshu.weathersnap.feature.weather.components.WeatherHeaderCard
import com.biprangshu.weathersnap.feature.weather.components.WeatherSuccessCard
import com.biprangshu.weathersnap.navigation.Screen
import com.biprangshu.weathersnap.ui.theme.DarkBackground
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun WeatherScreen(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        WeatherHeaderCard(
            onReportsClick = {
                haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                navController.navigate(Screen.SavedReports.route)
            },
        )

        SearchCard(
            query = uiState.searchQuery,
            isLoading = uiState.weatherState is WeatherState.Loading,
            onQueryChange = viewModel::onSearchQueryChanged,
            onSearchClick = {
                haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                viewModel.onRetry()
            },
        )

        AnimatedVisibility(
            visible = uiState.isSuggestionsDropdownVisible,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            SuggestionsCard(
                suggestionsState = uiState.suggestionsState,
                onCitySelected = { city ->
                    haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                    viewModel.onCitySelected(city)
                },
            )
        }

        AnimatedContent(
            targetState = uiState.weatherState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "weather_content",
        ) { state ->
            when (state) {
                WeatherState.Empty -> EmptyStateCard()
                WeatherState.Loading -> LoadingStateCard()
                is WeatherState.Error -> ErrorCard(
                    message = state.message,
                    onRetry = {
                        haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                        viewModel.onRetry()
                    },
                )
                is WeatherState.Success -> WeatherSuccessCard(
                    weather = state.weather,
                    onCreateReport = {
                        haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                        val json = Gson().toJson(state.weather)
                        val encoded = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                        navController.navigate(Screen.CreateReport.createRoute(encoded))
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}



