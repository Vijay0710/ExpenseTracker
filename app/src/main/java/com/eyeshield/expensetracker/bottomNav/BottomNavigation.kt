package com.eyeshield.expensetracker.bottomNav

import androidx.activity.compose.BackHandler
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.add.AddScreen
import com.eyeshield.expensetracker.application.ApplicationNavController
import com.eyeshield.expensetracker.calendar_graph.CalendarScreen
import com.eyeshield.expensetracker.calendar_graph.TransactionViewModel
import com.eyeshield.expensetracker.cards.CardScreen
import com.eyeshield.expensetracker.cards.CardsViewModel
import com.eyeshield.expensetracker.components.rememberCustomNavController
import com.eyeshield.expensetracker.data.local.database.orLoading
import com.eyeshield.expensetracker.extensions.bottomPadding
import com.eyeshield.expensetracker.extensions.endPadding
import com.eyeshield.expensetracker.extensions.startPadding
import com.eyeshield.expensetracker.home_graph.home.HomeScreen
import com.eyeshield.expensetracker.home_graph.home.HomeViewModel
import com.eyeshield.expensetracker.settings.SettingsScreen
import com.eyeshield.expensetracker.utils.leftPadding
import com.eyeshield.expensetracker.utils.rightPadding

@Composable
fun BottomNavigation(
    mainNavController: ApplicationNavController
) {
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
            NavigationBar(
                contentColor = Color.Transparent,
                containerColor = colorResource(R.color.shadow_white)
            ) {
                bottomNavItems.forEachIndexed { _, item ->
                    NavigationBarItem(
                        modifier = Modifier,
                        selected = remember(bottomNavController.bottomTabCurrentDestination) {
                            bottomNavController.bottomTabCurrentDestination == item
                        },
                        onClick = {
                            if (bottomNavController.bottomTabCurrentDestination != item) {
                                bottomNavController.navigate(item)
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
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = Color.Gray
                        )
                    )
                }
            }
        },
        containerColor = colorResource(R.color.shadow_white)
    ) { innerPadding ->
        NavHost(
            modifier = Modifier
                .startPadding(innerPadding.leftPadding())
                .endPadding(innerPadding.rightPadding())
                .bottomPadding(innerPadding.calculateBottomPadding()),
            navController = bottomNavController,
            startDestination = Tabs.HomeScreen,
        ) {
            composable<Tabs.HomeScreen> {
                val viewModel = hiltViewModel<HomeViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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

                val uiState by transactionViewModel.uiState.collectAsStateWithLifecycle()

                BackHandler {
                    bottomNavController.popUpToHomeScreen()
                }

                LaunchedEffect(Unit) {
                    transactionViewModel.getTransactions()
                }

                CalendarScreen(
                    getAllTransactions = transactionViewModel.databaseResult.value?._data,
                    uiState = uiState,
                    uiAction = transactionViewModel::onUiAction,
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
                val viewModel = hiltViewModel<CardsViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                CardScreen(
                    uiState = uiState,
                    uiAction = viewModel::onUiAction,
                )
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