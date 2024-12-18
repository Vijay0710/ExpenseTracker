package com.eyeshield.expensetracker.extensions

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry

fun slideInFromRightToLeft(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) {
    return {
        slideInHorizontally(
            animationSpec = tween(
                durationMillis = 500
            )
        ) { fullWidth ->
            fullWidth
        } +
                fadeIn(
                    animationSpec = tween(durationMillis = 500)
                )
    }
}

fun slideOutFromRightToLeft(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) {
    return {
        slideOutHorizontally(
            animationSpec = tween(
                durationMillis = 500
            )
        ) { fullWidth ->
            fullWidth
        } + fadeOut(
            animationSpec = tween(durationMillis = 500)
        )
    }
}

fun noExitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) {
    return {
        ExitTransition.None
    }
}

fun noEnterTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) {
    return {
        EnterTransition.None
    }
}

fun fadeAndZoomOutTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) {
    return {
        fadeOut(
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutLinearInEasing
            )
        ) +
                scaleOut(
                    targetScale = 0.55f,
                    animationSpec = tween(
                        durationMillis = 1000,
                        easing = FastOutLinearInEasing
                    )
                )
    }
}

fun fadeAndZoomInTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) {
    return {
        fadeIn(
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutLinearInEasing
            )
        ) +
                scaleIn(
                    initialScale = 0.95f,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutLinearInEasing
                    ),
                )
    }
}