package com.eyeshield.expensetracker.application

import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes {

    @Serializable
    data object BottomNavigation : Routes

    @Serializable
    data object StatisticsScreen : Routes

    @Serializable
    data object AddExpenseScreen : Routes

    @Serializable
    data object WelcomeScreen : Routes
}