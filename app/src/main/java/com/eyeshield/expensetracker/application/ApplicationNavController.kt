package com.eyeshield.expensetracker.application

import android.content.Context
import com.eyeshield.expensetracker.components.CustomNavHostController

class ApplicationNavController(context: Context) : CustomNavHostController(context) {

    private var lastNavigationTime = 0L

    /**
     * Ensures navigation of route specifically once even when user taps twice
     * **/
    fun navigateToSingleTop(route: MainNavRoutes) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastNavigationTime >= NAVIGATION_DELAY) {
            lastNavigationTime = currentTime
            navigate(route)
        }
    }

    companion object {
        private const val NAVIGATION_DELAY = 300L
    }
}