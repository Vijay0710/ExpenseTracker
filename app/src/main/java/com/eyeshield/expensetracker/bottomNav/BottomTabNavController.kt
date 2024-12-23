package com.eyeshield.expensetracker.bottomNav

import android.content.Context
import android.os.Bundle
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
     * This instance of navController is cleared so later when I pop or come to this screen it will only restore the values which are provided by NavHostController like
     * currentDestination, route internal values of those class
     * To save and restore values even after recreation I need to override the below methods and handle it in my own class
     */

    override fun saveState(): Bundle? {
        return super.saveState()?.apply {
            putString("bottomTabCurrentDestination", bottomTabCurrentDestination.toString())
        }
    }

    override fun restoreState(navState: Bundle?) {
        super.restoreState(navState)
        navState?.getString("bottomTabCurrentDestination")?.let {
            bottomTabCurrentDestination = when (it) {
                Tabs.HomeScreen.toString() -> Tabs.HomeScreen
                Tabs.CalendarScreen.toString() -> Tabs.CalendarScreen
                Tabs.AddScreen.toString() -> Tabs.AddScreen
                Tabs.CardScreen.toString() -> Tabs.CardScreen
                Tabs.SettingsScreen.toString() -> Tabs.SettingsScreen
                else -> Tabs.HomeScreen
            }
        }
    }

    fun navigate(route: Tabs) {
        navigate(route) {
            launchSingleTop = true
            updateBottomTabCurrentDestination(route)
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