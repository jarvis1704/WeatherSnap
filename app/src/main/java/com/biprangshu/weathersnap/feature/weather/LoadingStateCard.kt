package com.biprangshu.weathersnap.feature.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.biprangshu.weathersnap.ui.theme.DarkSurface
import com.biprangshu.weathersnap.ui.theme.DarkSurfaceVariant
import com.biprangshu.weathersnap.ui.theme.LimeAccent
import com.biprangshu.weathersnap.ui.theme.OnDarkSurface
import com.biprangshu.weathersnap.ui.theme.OnDarkSurfaceVariant

@Composable
fun LoadingStateCard() {
    Card(
        modifier = Modifier.Companion.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.Companion.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.Companion.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.Companion.size(32.dp),
                    color = LimeAccent,
                    strokeWidth = 3.dp,
                    trackColor = DarkSurfaceVariant,
                )
                Column {
                    Text(
                        text = "Loading weather...",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Companion.SemiBold,
                        color = OnDarkSurface,
                    )
                    Text(
                        text = "Fetching coordinates and current conditions",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnDarkSurfaceVariant,
                    )
                }
            }
            Spacer(modifier = Modifier.Companion.height(14.dp))
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier.Companion
                            .weight(1f)
                            .height(80.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                            .background(DarkSurfaceVariant),
                    )
                }
            }
        }
    }
}