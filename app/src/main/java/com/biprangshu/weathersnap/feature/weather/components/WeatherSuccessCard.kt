package com.biprangshu.weathersnap.feature.weather.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.biprangshu.weathersnap.ui.theme.DarkOliveText
import com.biprangshu.weathersnap.ui.theme.DarkSurface
import com.biprangshu.weathersnap.ui.theme.DarkSurfaceVariant
import com.biprangshu.weathersnap.ui.theme.HumidityColor
import com.biprangshu.weathersnap.ui.theme.LimeAccent
import com.biprangshu.weathersnap.ui.theme.OnDarkSurface
import com.biprangshu.weathersnap.ui.theme.OnDarkSurfaceVariant
import com.biprangshu.weathersnap.ui.theme.PressureColor
import com.biprangshu.weathersnap.ui.theme.TempBadgeBg
import com.biprangshu.weathersnap.ui.theme.WindColor

@Composable
fun WeatherSuccessCard(
    weather: Weather,
    onCreateReport: () -> Unit,
) {
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = weather.cityName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = OnDarkSurface,
                    )
                    Text(
                        text = weather.condition,
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnDarkSurfaceVariant,
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                        .background(TempBadgeBg)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "${weather.temperatureCelsius.toInt()}°C",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MetricCard(
                    label = "Humidity",
                    value = "${weather.humidityPercent}%",
                    valueColor = HumidityColor,
                    modifier = Modifier.weight(1f),
                )
                MetricCard(
                    label = "Wind",
                    value = "${String.format("%.2f", weather.windSpeedKmh)} km/h",
                    valueColor = WindColor,
                    modifier = Modifier.Companion.weight(1f),
                )
                MetricCard(
                    label = "Pressure",
                    value = "${weather.pressureHpa.toInt()}",
                    valueColor = PressureColor,
                    modifier = Modifier.weight(1f),
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                    .background(DarkSurfaceVariant)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Report readiness",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnDarkSurfaceVariant,
                    )
                    Text(
                        text = "Camera and Room DB enabled",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Companion.SemiBold,
                        color = OnDarkSurface,
                    )
                }
            }

            Button(
                onClick = onCreateReport,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LimeAccent,
                    contentColor = DarkOliveText,
                ),
            ) {
                Text(
                    text = "Create Report",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun MetricCard(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
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
                fontWeight = FontWeight.Bold,
                color = valueColor,
            )
        }
    }
}