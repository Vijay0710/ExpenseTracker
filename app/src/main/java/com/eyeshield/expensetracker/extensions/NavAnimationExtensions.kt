package com.eyeshield.expensetracker.extensions

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

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

fun slideOutOfContainerFromRightToLeft(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) {
    return {
        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(400))
    }
}

fun noExitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) {
    return {
        ExitTransition.None
    }
}

fun fadeOutExitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) {
    return {
        fadeOut(animationSpec = tween(400, easing = FastOutLinearInEasing))
    }
}