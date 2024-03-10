package com.eyeshield.expensetracker

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.eyeshield.expensetracker.bottomnav.Screens

object NavigationExtensions {

    fun NavController.popUpToHomeScreen(
        popRoute: String = Screens.HomeScreen.route,
        route: String = Screens.HomeScreen.route
    ) {
        navigate(route) {
            popUpTo(popRoute) {
                inclusive = true
            }
        }
    }

    fun slideIntoContainerFromRightToLeft(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) {
        return {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(500))
        }
    }

    fun slideIntoContainerFromLeftToRight(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) {
        return {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(500))
        }
    }
}