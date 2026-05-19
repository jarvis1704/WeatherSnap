package com.biprangshu.weathersnap.feature.weather.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.biprangshu.weathersnap.ui.theme.BannerGradientEnd
import com.biprangshu.weathersnap.ui.theme.BannerGradientStart
import com.biprangshu.weathersnap.ui.theme.DarkSurface
import com.biprangshu.weathersnap.ui.theme.OnDarkSurface
import com.biprangshu.weathersnap.ui.theme.OnDarkSurfaceVariant

@Composable
fun EmptyStateCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(BannerGradientStart, BannerGradientEnd),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Search. Capture. Save.",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "No weather loaded",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = OnDarkSurface,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Enter more than 2 letters, choose a city, then search.",
                style = MaterialTheme.typography.bodySmall,
                color = OnDarkSurfaceVariant,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}