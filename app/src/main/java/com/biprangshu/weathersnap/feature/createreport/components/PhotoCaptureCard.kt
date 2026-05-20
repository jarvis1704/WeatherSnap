package com.biprangshu.weathersnap.feature.createreport.components

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.biprangshu.weathersnap.feature.createreport.CompressionInfo
import com.biprangshu.weathersnap.ui.theme.BannerGradientEnd
import com.biprangshu.weathersnap.ui.theme.BannerGradientStart
import com.biprangshu.weathersnap.ui.theme.DarkOliveText
import com.biprangshu.weathersnap.ui.theme.DarkSurface
import com.biprangshu.weathersnap.ui.theme.HumidityColor
import com.biprangshu.weathersnap.ui.theme.LimeAccent
import com.biprangshu.weathersnap.ui.theme.OnDarkSurfaceVariant
import com.biprangshu.weathersnap.ui.theme.PressureColor

@Composable
fun PhotoCaptureCard(
    imagePath: String?,
    onCaptureClick: () -> Unit,
    compressedSize: Long,
    originalSize: Long,
) {

    val compressedSizeInBytes = compressedSize/1000L
    val originalSizeInBytes = originalSize/1000L

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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(BannerGradientStart, BannerGradientEnd),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (imagePath != null) {
                    AsyncImage(
                        model = imagePath,
                        contentDescription = "Captured photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Text(
                        text = "Photo preview",
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnDarkSurfaceVariant,
                    )
                }
            }

            //compression info
            if (imagePath!=null){
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    //original size box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PressureColor.copy(
                                alpha = 0.1f
                            )),
                        contentAlignment = Alignment.Center
                    ){
                        Column(
                            modifier = Modifier.fillMaxSize().padding(8.dp)
                        ) {
                            Text(
                                text = "Original Size",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium,
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "${originalSizeInBytes}KB",
                                color = PressureColor,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                    }

                    //compressed size box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(HumidityColor.copy(
                                alpha = 0.1f
                            ))
                        ,
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(8.dp)
                        ) {
                            Text(
                                text = "Compressed Size",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium,
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "${compressedSizeInBytes}KB",
                                color = HumidityColor,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }


                    }
                }
            }


            Button(
                onClick = onCaptureClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LimeAccent,
                    contentColor = DarkOliveText,
                ),
            ) {
                Text(
                    text = if (imagePath != null) "Retake Photo" else "Capture Photo",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}