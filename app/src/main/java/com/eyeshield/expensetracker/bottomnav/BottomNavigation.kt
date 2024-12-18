package com.eyeshield.expensetracker.bottomnav

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.add.AddScreen
import com.eyeshield.expensetracker.application.ApplicationNavController
import com.eyeshield.expensetracker.calendar_graph.CalendarScreen
import com.eyeshield.expensetracker.calendar_graph.TransactionViewModel
import com.eyeshield.expensetracker.cards.CardScreen
import com.eyeshield.expensetracker.components.rememberCustomNavController
import com.eyeshield.expensetracker.database.orLoading
import com.eyeshield.expensetracker.home_graph.home.HomeScreen
import com.eyeshield.expensetracker.home_graph.home.HomeViewModel
import com.eyeshield.expensetracker.settings.SettingsScreen

@Composable
fun BottomNavigation(mainNavController: ApplicationNavController) {
    val bottomNavController = rememberCustomNavController<BottomTabNavController>()

    val bottomNavItems = remember {
        listOf(
            Tabs.HomeScreen,
            Tabs.CalendarScreen,
            Tabs.AddScreen,
            Tabs.CardScreen,
            Tabs.SettingsScreen
        )
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.Transparent) {
                bottomNavItems.forEachIndexed { _, item ->
                    NavigationBarItem(
                        modifier = Modifier,
                        selected = remember(bottomNavController.bottomTabCurrentDestination) {
                            bottomNavController.bottomTabCurrentDestination == item
                        },
                        onClick = {
                            if (bottomNavController.bottomTabCurrentDestination != item) {
                                bottomNavController.navigate(
                                    route = item
                                )
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
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier
                .background(color = colorResource(id = R.color.shadow_white))
                .padding(innerPadding),
            navController = bottomNavController,
            startDestination = Tabs.HomeScreen,
        ) {
            composable<Tabs.HomeScreen> {
                val viewModel = hiltViewModel<HomeViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle(
                    LocalLifecycleOwner.current
                )

                HomeScreen(
                    onNavigate = { route ->
                        mainNavController.navigateToSingleTop(route)
                    },
                    uiState = uiState,
                    uiAction = viewModel::onUiAction
                )
            }
            composable<Tabs.CalendarScreen> {
                val transactionViewModel = hiltViewModel<TransactionViewModel>()

                BackHandler { bottomNavController.popUpToHomeScreen() }

                LaunchedEffect(Unit) {
                    transactionViewModel.getTransactions()
                }

                CalendarScreen(
                    getAllTransactions = transactionViewModel.databaseResult.value?._data,
                    databaseStatus = transactionViewModel.databaseResult.value?.status.orLoading(),
                    onNavigate = { route ->
                        mainNavController.navigateToSingleTop(route)
                    }
                )
            }
            composable<Tabs.AddScreen> {
                BackHandler {
                    bottomNavController.popUpToHomeScreen()
                }
                AddScreen()
            }
            composable<Tabs.CardScreen> {
                BackHandler {
                    bottomNavController.popUpToHomeScreen()
                }
                CardScreen()
            }
            composable<Tabs.SettingsScreen> {
                BackHandler {
                    bottomNavController.popUpToHomeScreen()
                }
                SettingsScreen()
            }
        }
    }
}