package com.biprangshu.weathersnap.feature.createreport.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biprangshu.weathersnap.domain.model.Weather
import com.biprangshu.weathersnap.ui.theme.DarkSurface
import com.biprangshu.weathersnap.ui.theme.DarkSurfaceVariant
import com.biprangshu.weathersnap.ui.theme.HumidityColor
import com.biprangshu.weathersnap.ui.theme.OnDarkSurface
import com.biprangshu.weathersnap.ui.theme.OnDarkSurfaceVariant
import com.biprangshu.weathersnap.ui.theme.PressureColor
import com.biprangshu.weathersnap.ui.theme.TempBadgeBg
import com.biprangshu.weathersnap.ui.theme.WindColor

@Composable
fun WeatherInfoCard(weather: Weather) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Companion.Top,
            ) {
                Column(modifier = Modifier.Companion.weight(1f)) {
                    Text(
                        text = weather.cityName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Companion.Bold,
                        color = OnDarkSurface,
                    )
                    Text(
                        text = weather.condition,
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnDarkSurfaceVariant,
                    )
                }
                Box(
                    modifier = Modifier.Companion
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                        .background(TempBadgeBg)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Companion.Center,
                ) {
                    Text(
                        text = "${weather.temperatureCelsius.toInt()}°C",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Companion.Bold,
                        color = Color.Companion.White,
                    )
                }
            }

            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ReportMetricCard(
                    label = "Humidity",
                    value = "${weather.humidityPercent}%",
                    valueColor = HumidityColor,
                    modifier = Modifier.Companion.weight(1f),
                )
                ReportMetricCard(
                    label = "Wind",
                    value = "${String.format("%.2f", weather.windSpeedKmh)} km/h",
                    valueColor = WindColor,
                    modifier = Modifier.Companion.weight(1f),
                )
                ReportMetricCard(
                    label = "Pressure",
                    value = "${weather.pressureHpa.toInt()}",
                    valueColor = PressureColor,
                    modifier = Modifier.Companion.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun ReportMetricCard(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceVariant)
            .padding(horizontal = 10.dp, vertical = 12.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = OnDarkSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Companion.Bold,
                color = valueColor,
            )
        }
    }
}