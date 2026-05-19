package com.biprangshu.weathersnap.feature.weather

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.biprangshu.weathersnap.domain.model.City
import com.biprangshu.weathersnap.domain.model.Weather
import com.biprangshu.weathersnap.navigation.Screen
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "WeatherSnap",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "Live weather reports and camera archive",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { navController.navigate(Screen.SavedReports.route) }) {
                        Text("Reports")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            SearchSection(
                query = uiState.searchQuery,
                suggestionsState = uiState.suggestionsState,
                isSuggestionsVisible = uiState.isSuggestionsDropdownVisible,
                onQueryChange = viewModel::onSearchQueryChanged,
                onCitySelected = viewModel::onCitySelected,
                onDismiss = viewModel::onSuggestionsDismissed,
            )

            Spacer(modifier = Modifier.height(16.dp))

            WeatherContent(
                weatherState = uiState.weatherState,
                onRetry = viewModel::onRetry,
                onCreateReport = { weather ->
                    val json = Gson().toJson(weather)
                    val encoded = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                    navController.navigate(Screen.CreateReport.createRoute(encoded))
                },
                onViewReports = { navController.navigate(Screen.SavedReports.route) },
            )
        }
    }
}

@Composable
private fun SearchSection(
    query: String,
    suggestionsState: SuggestionsState,
    isSuggestionsVisible: Boolean,
    onQueryChange: (String) -> Unit,
    onCitySelected: (City) -> Unit,
    onDismiss: () -> Unit,
) {
    Column {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("City") },
            placeholder = { Text("e.g. London") },
            trailingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            },
            supportingText = {
                Text("Enter more than 2 letters to start city suggestions.")
            },
            singleLine = true,
        )

        AnimatedVisibility(
            visible = isSuggestionsVisible,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
                if (suggestionsState is SuggestionsState.Success) {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(suggestionsState.cities) { city ->
                            CityItem(city = city, onClick = { onCitySelected(city) })
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CityItem(city: City, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(text = city.name, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = listOfNotNull(city.region.takeIf { it.isNotEmpty() }, city.country.takeIf { it.isNotEmpty() })
                .joinToString(", "),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun WeatherContent(
    weatherState: WeatherState,
    onRetry: () -> Unit,
    onCreateReport: (Weather) -> Unit,
    onViewReports: () -> Unit,
) {
    AnimatedContent(
        targetState = weatherState,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "weather_content",
    ) { state ->
        when (state) {
            WeatherState.Empty -> EmptyState()
            WeatherState.Loading -> LoadingState()
            is WeatherState.Error -> ErrorState(message = state.message, onRetry = onRetry)
            is WeatherState.Success -> WeatherSuccessContent(
                weather = state.weather,
                onCreateReport = { onCreateReport(state.weather) },
                onViewReports = onViewReports,
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Box(modifier = Modifier.fillMaxWidth().padding(top = 48.dp), contentAlignment = Alignment.Center) {
        Text(
            text = "Search for a city to see weather",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun LoadingState() {
    Box(modifier = Modifier.fillMaxWidth().padding(top = 48.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Failed to load weather",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error,
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        OutlinedButton(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
private fun WeatherSuccessContent(
    weather: Weather,
    onCreateReport: () -> Unit,
    onViewReports: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        WeatherCard(weather = weather)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedButton(
                onClick = onViewReports,
                modifier = Modifier.weight(1f),
            ) {
                Text("Reports")
            }
            Button(
                onClick = onCreateReport,
                modifier = Modifier.weight(1f),
            ) {
                Text("Create Report")
            }
        }
    }
}

@Composable
private fun WeatherCard(weather: Weather) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column {
                    Text(
                        text = weather.cityName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = weather.condition,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = "${weather.temperatureCelsius.toInt()}°C",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SuggestionChip(
                    onClick = {},
                    label = { Text("Humidity ${weather.humidityPercent}%") },
                )
                SuggestionChip(
                    onClick = {},
                    label = { Text("Wind ${weather.windSpeedKmh.toInt()} km/h") },
                )
                SuggestionChip(
                    onClick = {},
                    label = { Text("${weather.pressureHpa.toInt()} hPa") },
                )
            }
        }
    }
}
