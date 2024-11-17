package com.eyeshield.expensetracker

import kotlinx.serialization.Serializable

@Serializable
sealed class MainNavRoutes {

    @Serializable
    data object BottomNavigation : MainNavRoutes()

    @Serializable
    data object StatisticsScreen : MainNavRoutes()

    @Serializable
    data object AddExpenseScreen : MainNavRoutes()
}