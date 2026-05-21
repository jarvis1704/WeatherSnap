package com.biprangshu.weathersnap.navigation

sealed class Screen(val route: String) {
    data object Weather : Screen("weather")
    data object SavedReports : Screen("saved_reports")
    data object SaveConfirmation : Screen("save_confirmation")
    data object Camera : Screen("camera")
    data object CreateReport : Screen("create_report/{weatherJson}") {
        fun createRoute(weatherJson: String) = "create_report/$weatherJson"
    }
}