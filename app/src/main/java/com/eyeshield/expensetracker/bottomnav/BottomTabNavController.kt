package com.eyeshield.expensetracker.bottomnav

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.eyeshield.expensetracker.components.CustomNavHostController

class BottomTabNavController(context: Context) : CustomNavHostController(context) {

    var bottomTabCurrentDestination by mutableStateOf<Tabs>(Tabs.HomeScreen)
        private set

    fun popUpToHomeScreen() {
        navigate(Tabs.HomeScreen) {
            popUpTo(Tabs.HomeScreen) {
                inclusive = true
                updateBottomTabCurrentDestination(Tabs.HomeScreen)
            }
        }
    }

    /**
     * Navigates to a given route and pops up to a give route which is inclusive
     * **/
    fun navigateAndPopUpToRoute(route: Tabs, popRoute: Tabs) {
        navigate(route) {
            popUpTo(popRoute) {
                inclusive = true
                updateBottomTabCurrentDestination(route)
            }
        }
    }

    private fun updateBottomTabCurrentDestination(destination: Tabs) {
        bottomTabCurrentDestination = destination
    }
}