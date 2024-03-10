package com.eyeshield.expensetracker

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.eyeshield.expensetracker.NavigationExtensions.popUpToHomeScreen
import com.eyeshield.expensetracker.add.AddScreen
import com.eyeshield.expensetracker.bottomnav.Screens
import com.eyeshield.expensetracker.calendar.CalendarScreen
import com.eyeshield.expensetracker.cards.CardScreen
import com.eyeshield.expensetracker.home_graph.compose.home.HomeScreen
import com.eyeshield.expensetracker.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigation(mainNavController : NavController) {

    val navController = rememberNavController()

    val bottomNavItems = listOf(
        Screens.HomeScreen,
        Screens.CalendarScreen,
        Screens.AddScreen,
        Screens.CardScreen,
        Screens.SettingsScreen
    )

    Scaffold(bottomBar = {
        CompositionLocalProvider(
            LocalRippleTheme provides ClearRippleTheme
        ) {
            NavigationBar(containerColor = Color.Transparent) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        modifier = Modifier,
                        selected = currentDestination?.route == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(item.route) {
                                    inclusive = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(id = item.icon),
                                contentDescription = stringResource(id = item.resourceId)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = colorResource(id = R.color.shadow_white),
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Gray
                        ),
                    )
                }
            }
        }
    }) { innerPadding ->

        NavHost(
            modifier = Modifier
                .background(color = colorResource(id = R.color.shadow_white))
                .padding(innerPadding),
            navController = navController,
            startDestination = Screens.HomeScreen.route,
        ) {
            composable(Screens.HomeScreen.route) {
                HomeScreen(mainNavController)
            }
            composable(Screens.CalendarScreen.route) {
                BackHandler { navController.popUpToHomeScreen() }
                CalendarScreen()
            }
            composable(Screens.AddScreen.route) {
                BackHandler { navController.popUpToHomeScreen() }
                AddScreen()
            }
            composable(Screens.CardScreen.route) {
                BackHandler { navController.popUpToHomeScreen() }
                CardScreen()
            }
            composable(Screens.SettingsScreen.route) {
                BackHandler { navController.popUpToHomeScreen() }
                SettingsScreen()
            }
        }
    }
}

object ClearRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color = Color.Transparent

    @Composable
    override fun rippleAlpha() = RippleAlpha(
        draggedAlpha = 0.0f,
        focusedAlpha = 0.0f,
        hoveredAlpha = 0.0f,
        pressedAlpha = 0.0f,
    )
}