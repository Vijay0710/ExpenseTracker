package com.eyeshield.expensetracker

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eyeshield.expensetracker.NavigationExtensions.popUpToHomeScreen
import com.eyeshield.expensetracker.add.AddScreen
import com.eyeshield.expensetracker.bottomnav.Screens
import com.eyeshield.expensetracker.calendar.CalendarScreen
import com.eyeshield.expensetracker.calendar.TransactionViewModel
import com.eyeshield.expensetracker.cards.CardScreen
import com.eyeshield.expensetracker.home_graph.compose.home.HomeScreen
import com.eyeshield.expensetracker.settings.SettingsScreen

@Composable
fun BottomNavigation(mainNavController: NavController) {

    val navController = rememberNavController()

    val bottomNavItems = listOf(
        Screens.HomeScreen,
        Screens.CalendarScreen,
        Screens.AddScreen,
        Screens.CardScreen,
        Screens.SettingsScreen
    )
    var currentDestination by remember {
        mutableStateOf<Screens>(Screens.HomeScreen)
    }

    Scaffold(bottomBar = {
        NavigationBar(containerColor = Color.Transparent) {
            bottomNavItems.forEachIndexed { _, item ->
                NavigationBarItem(
                    modifier = Modifier,
                    selected = currentDestination == item,
                    onClick = {
                        if (currentDestination != item) {
                            navController.navigate(item) {
                                popUpTo(item) {
                                    inclusive = true
                                }
                            }
                            currentDestination = item
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

    }) { innerPadding ->

        NavHost(
            modifier = Modifier
                .background(color = colorResource(id = R.color.shadow_white))
                .padding(innerPadding),
            navController = navController,
            startDestination = Screens.HomeScreen,
        ) {
            composable<Screens.HomeScreen> {
                HomeScreen(mainNavController)
            }
            composable<Screens.CalendarScreen> {
                BackHandler { navController.popUpToHomeScreen() }

                val transactionViewModel: TransactionViewModel = hiltViewModel()

                LaunchedEffect(key1 = Unit) {
                    transactionViewModel.getTransactions()
                }

                CalendarScreen(
                    onAddTransaction = {
                        transactionViewModel.recordATransaction(it)
                    },
                    getAllTransactions = transactionViewModel.databaseResult.value.data,
                    databaseStatus = transactionViewModel.databaseStatus.value,
                    mainNavController
                )
            }
            composable<Screens.AddScreen> {
                BackHandler { navController.popUpToHomeScreen() }
                AddScreen()
            }
            composable<Screens.CardScreen> {
                BackHandler { navController.popUpToHomeScreen() }
                CardScreen()
            }
            composable<Screens.SettingsScreen> {
                BackHandler { navController.popUpToHomeScreen() }
                SettingsScreen()
            }
        }
    }
}