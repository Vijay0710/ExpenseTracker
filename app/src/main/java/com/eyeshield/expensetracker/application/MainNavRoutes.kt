package com.eyeshield.expensetracker.application

import kotlinx.serialization.Serializable

@Serializable
sealed interface MainNavRoutes {

    @Serializable
    data object BottomNavigation : MainNavRoutes

    @Serializable
    data object StatisticsScreen : MainNavRoutes

    @Serializable
    data object AddExpenseScreen : MainNavRoutes
}