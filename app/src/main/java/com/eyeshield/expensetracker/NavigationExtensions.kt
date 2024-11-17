package com.eyeshield.expensetracker

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.eyeshield.expensetracker.bottomnav.Tabs

object NavigationExtensions {

    fun NavController.popUpToHomeScreen(
        popRoute: Tabs = Tabs.HomeScreen,
        route: Tabs = Tabs.HomeScreen
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