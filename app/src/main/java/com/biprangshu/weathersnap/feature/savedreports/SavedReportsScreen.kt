package com.biprangshu.weathersnap.feature.savedreports

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.biprangshu.weathersnap.domain.model.Report
import com.biprangshu.weathersnap.feature.savedreports.components.ReportCard
import com.biprangshu.weathersnap.feature.savedreports.viewmodel.SavedReportsViewModel
import com.biprangshu.weathersnap.ui.theme.DarkBackground
import com.biprangshu.weathersnap.ui.theme.DarkOliveText
import com.biprangshu.weathersnap.ui.theme.DarkSurface
import com.biprangshu.weathersnap.ui.theme.DarkSurfaceVariant
import com.biprangshu.weathersnap.ui.theme.HeaderGradientEnd
import com.biprangshu.weathersnap.ui.theme.HeaderGradientStart
import com.biprangshu.weathersnap.ui.theme.HumidityColor
import com.biprangshu.weathersnap.ui.theme.LimeAccent
import com.biprangshu.weathersnap.ui.theme.OnDarkSurface
import com.biprangshu.weathersnap.ui.theme.OnDarkSurfaceVariant
import com.biprangshu.weathersnap.ui.theme.PressureColor
import com.biprangshu.weathersnap.ui.theme.ReportsButtonBg
import com.biprangshu.weathersnap.ui.theme.ReportsButtonText
import com.biprangshu.weathersnap.ui.theme.TempBadgeBg
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun SavedReportScreen(
    navController: NavController,
    viewModel: SavedReportsViewModel = hiltViewModel(),
) {
    val reports by viewModel.reports.collectAsStateWithLifecycle()

    var cardVisible by remember { mutableStateOf(false) }
    LaunchedEffect(reports) {
        if (reports.isEmpty()) cardVisible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .navigationBarsPadding(),
    ) {
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
                        text = "Saved Reports",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = DarkOliveText,
                    )
                    Text(
                        text = "${reports.size} reports stored locally",
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

        //condition when no report is saved
        if (reports.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "No reports saved yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnDarkSurfaceVariant,
                )
            }
        } else {
            Column(
                modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())
            ) {
                reports.forEach {
                    report->
                    AnimatedVisibility(
                        visible = cardVisible,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 }),
                    ) {
                        ReportCard(report = report)
                    }
                }
            }
        }
    }
}