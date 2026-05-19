package com.biprangshu.weathersnap.feature.weather.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.biprangshu.weathersnap.ui.theme.DarkOliveText
import com.biprangshu.weathersnap.ui.theme.HeaderGradientEnd
import com.biprangshu.weathersnap.ui.theme.HeaderGradientStart
import com.biprangshu.weathersnap.ui.theme.ReportsButtonBg
import com.biprangshu.weathersnap.ui.theme.ReportsButtonText


//header composable for weather card
@Composable
fun WeatherHeaderCard(onReportsClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(HeaderGradientStart, HeaderGradientEnd),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, 0f),
                    ),
                )
                .padding(horizontal = 20.dp, vertical = 18.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "WeatherSnap",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkOliveText,
                    )
                    Text(
                        text = "Live weather reports with camera evidence",
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkOliveText.copy(alpha = 0.75f),
                    )
                }
                Button(
                    onClick = onReportsClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ReportsButtonBg,
                        contentColor = ReportsButtonText,
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = "Reports",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}
