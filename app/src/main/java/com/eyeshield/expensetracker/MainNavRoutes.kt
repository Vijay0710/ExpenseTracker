package com.eyeshield.expensetracker

sealed class MainNavRoutes(val route: String) {
    object BottomNavigation : MainNavRoutes("bottom_nav")
    object StatisticsScreen : MainNavRoutes("statistics")
}