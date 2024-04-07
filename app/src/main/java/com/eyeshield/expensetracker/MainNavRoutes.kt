package com.eyeshield.expensetracker

sealed class MainNavRoutes(val route: String) {

    data object BottomNavigation : MainNavRoutes("bottom_nav")

    data object StatisticsScreen : MainNavRoutes("statistics")

    data object AddExpenseScreen: MainNavRoutes("add_expense_screen")
}