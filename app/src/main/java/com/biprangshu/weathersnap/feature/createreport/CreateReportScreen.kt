package com.biprangshu.weathersnap.feature.createreport

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.biprangshu.weathersnap.feature.createreport.components.FieldNotesCard
import com.biprangshu.weathersnap.feature.createreport.components.PhotoCaptureCard
import com.biprangshu.weathersnap.feature.createreport.components.WeatherInfoCard
import com.biprangshu.weathersnap.navigation.Screen
import com.biprangshu.weathersnap.ui.theme.DarkBackground
import com.biprangshu.weathersnap.ui.theme.DarkOliveText
import com.biprangshu.weathersnap.ui.theme.HeaderGradientEnd
import com.biprangshu.weathersnap.ui.theme.HeaderGradientStart
import com.biprangshu.weathersnap.ui.theme.LimeAccent
import com.biprangshu.weathersnap.ui.theme.ReportsButtonBg
import com.biprangshu.weathersnap.ui.theme.ReportsButtonText

@Composable
fun CreateReportScreen(
    navController: NavController,
    weatherJson: String,
    viewModel: CreateReportViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val compressionInfo by viewModel.compressionInfo.collectAsStateWithLifecycle()

    //for getting the image path right after capturing the image in viewfinder
    LaunchedEffect(Unit) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow<String?>("imagePath", null)
            ?.collect { path ->
                if (path != null) {
                    viewModel.onImageCaptured(path)
                    navController.currentBackStackEntry?.savedStateHandle?.set("imagePath", null)
                }
            }
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            navController.navigate(Screen.SaveConfirmation.route) {
                popUpTo(Screen.CreateReport.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding(),
    ) {
        //hero
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(HeaderGradientStart, HeaderGradientEnd),
                    ),
                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                )
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 20.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Create Report",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = DarkOliveText,
                    )
                    Text(
                        text = "Capture, compress, annotate",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkOliveText.copy(alpha = 0.7f),
                    )
                }
                //back button
                Button(
                    onClick = { navController.popBackStack() },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ReportsButtonBg,
                        contentColor = ReportsButtonText,
                    ),
                ) {
                    Text(
                        text = "Back",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(modifier = Modifier.height(12.dp))

            //weather info
            state.weather?.let { weather ->
                WeatherInfoCard(weather = weather)
            }

            //capture card
            PhotoCaptureCard(
                imagePath = state.capturedImagePath,
                onCaptureClick = { navController.navigate(Screen.Camera.route) },
                compressedSize = compressionInfo.compressedSize,
                originalSize = compressionInfo.originalSize

            )

            //notes card
            FieldNotesCard(
                notes = state.notes,
                onNotesChanged = viewModel::onNotesChanged,
            )

            //save button
            Button(
                onClick = viewModel::onSaveReport,
                enabled = !state.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LimeAccent,
                    contentColor = DarkOliveText,
                    disabledContainerColor = LimeAccent.copy(alpha = 0.5f),
                    disabledContentColor = DarkOliveText.copy(alpha = 0.5f),
                ),
            ) {
                Text(
                    text = if (state.isSaving) "Saving…" else "Save Report",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Box(modifier = Modifier.height(8.dp))
        }
    }
}

