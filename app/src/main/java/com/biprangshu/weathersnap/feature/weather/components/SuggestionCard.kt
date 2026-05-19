package com.biprangshu.weathersnap.feature.weather.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.biprangshu.weathersnap.domain.model.City
import com.biprangshu.weathersnap.feature.weather.SuggestionsState
import com.biprangshu.weathersnap.ui.theme.DarkSurface
import com.biprangshu.weathersnap.ui.theme.DarkSurfaceVariant
import com.biprangshu.weathersnap.ui.theme.OnDarkSurface


//sugestion card dynamically appears on search result success
@Composable
fun SuggestionsCard(
    suggestionsState: SuggestionsState,
    onCitySelected: (City) -> Unit,
) {
    if (suggestionsState !is SuggestionsState.Success) return

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            suggestionsState.cities.forEach { city ->
                SuggestionItem(city = city, onClick = { onCitySelected(city) })
            }
        }
    }
}

@Composable
private fun SuggestionItem(city: City, onClick: () -> Unit) {
    val label = buildString {
        append(city.name)
        if (city.country.isNotEmpty()) append(", ${city.country}")
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceVariant)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = OnDarkSurface,
            textAlign = TextAlign.Center,
        )
    }
}