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

    @Serializable
    data object AuthRoute : MainNavRoutes
}


sealed interface AuthRoutes {

    @Serializable
    data object WelcomeScreen : AuthRoutes

    @Serializable
    data object LoginScreen : AuthRoutes

    @Serializable
    data object RegisterScreen : AuthRoutes


}