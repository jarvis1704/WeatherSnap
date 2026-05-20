package com.biprangshu.weathersnap.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.biprangshu.weathersnap.feature.camera.CameraScreen
import com.biprangshu.weathersnap.feature.createreport.CreateReportScreen
import com.biprangshu.weathersnap.feature.savedreports.SaveConfirmationScreen
import com.biprangshu.weathersnap.feature.savedreports.SavedReportsScreen
import com.biprangshu.weathersnap.feature.weather.WeatherScreen

sealed class Screen(val route: String) {
    data object Weather : Screen("weather")
    data object SavedReports : Screen("saved_reports")
    data object SaveConfirmation : Screen("save_confirmation")
    data object Camera : Screen("camera")
    data object CreateReport : Screen("create_report/{weatherJson}") {
        fun createRoute(weatherJson: String) = "create_report/$weatherJson"
    }
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Weather.route,
    ) {
        composable(Screen.Weather.route) {
            WeatherScreen(navController = navController)
        }

        composable(
            route = Screen.CreateReport.route,
            arguments = listOf(
                navArgument("weatherJson") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val weatherJson = backStackEntry.arguments?.getString("weatherJson") ?: ""
            CreateReportScreen(navController = navController, weatherJson = weatherJson)
        }

        composable(Screen.Camera.route) {
            CameraScreen(navController = navController)
        }

        composable(Screen.SavedReports.route) {
            SavedReportsScreen(navController = navController)
        }

        composable(Screen.SaveConfirmation.route) {
            SaveConfirmationScreen(navController = navController)
        }
    }
}
