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
import com.biprangshu.weathersnap.feature.savedreports.SavedReportScreen
import com.biprangshu.weathersnap.feature.weather.WeatherScreen



@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Weather.route,
        enterTransition = horizontalEnter,
        exitTransition = horizontalExit,
        popEnterTransition = horizontalPopEnter,
        popExitTransition = horizontalPopExit,
    ) {
        composable(
            route = Screen.Weather.route,
            // Modal destinations slide up — don't slide Weather left behind them
            exitTransition = {
                val dest = targetState.destination.route
                if (dest == Screen.CreateReport.route || dest == Screen.SaveConfirmation.route)
                    run(modalExit)
                else
                    run(horizontalExit)
            },
            // Modal screens slide down on dismiss — Weather stays fixed, just fades back in
            popEnterTransition = {
                when (initialState.destination.route) {
                    Screen.SaveConfirmation.route -> run(scalePopEnter)
                    Screen.CreateReport.route -> run(modalPopEnter)
                    else -> run(horizontalPopEnter)
                }
            },
        ) {
            WeatherScreen(navController = navController)
        }

        composable(
            route = Screen.CreateReport.route,
            arguments = listOf(
                navArgument("weatherJson") { type = NavType.StringType },
            ),
            enterTransition = modalEnter,
            exitTransition = modalExit,
            popEnterTransition = modalPopEnter,
            popExitTransition = modalPopExit,
        ) { backStackEntry ->
            val weatherJson = backStackEntry.arguments?.getString("weatherJson") ?: ""
            CreateReportScreen(navController = navController, weatherJson = weatherJson)
        }

        composable(
            route = Screen.Camera.route,
            enterTransition = modalEnter,
            exitTransition = modalExit,
            popEnterTransition = modalPopEnter,
            popExitTransition = modalPopExit,
        ) {
            CameraScreen(navController = navController)
        }

        composable(Screen.SavedReports.route) {
            SavedReportScreen(navController = navController)
        }

        composable(
            route = Screen.SaveConfirmation.route,
            enterTransition = modalEnter,
            exitTransition = modalExit,
            popEnterTransition = modalPopEnter,
            popExitTransition = modalPopExit,
        ) {
            SaveConfirmationScreen(navController = navController)
        }
    }
}
