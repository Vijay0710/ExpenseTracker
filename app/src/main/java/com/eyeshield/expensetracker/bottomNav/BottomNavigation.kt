package com.eyeshield.expensetracker.bottomNav

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eyeshield.expensetracker.add.AddScreen
import com.eyeshield.expensetracker.application.ApplicationNavController
import com.eyeshield.expensetracker.calendar_graph.CalendarScreen
import com.eyeshield.expensetracker.calendar_graph.TransactionViewModel
import com.eyeshield.expensetracker.cards.CardScreen
import com.eyeshield.expensetracker.cards.CardsViewModel
import com.eyeshield.expensetracker.common.NetworkConnectivity
import com.eyeshield.expensetracker.data.local.database.orLoading
import com.eyeshield.expensetracker.extensions.bottomPadding
import com.eyeshield.expensetracker.extensions.horizontalPadding
import com.eyeshield.expensetracker.extensions.topPadding
import com.eyeshield.expensetracker.home_graph.home.HomeScreen
import com.eyeshield.expensetracker.home_graph.home.HomeViewModel
import com.eyeshield.expensetracker.settings.SettingsScreen
import com.eyeshield.expensetracker.utils.hasCameraNotch

@Composable
fun BottomNavigation(
    mainNavController: ApplicationNavController,
    bottomNavController: BottomTabNavController,
    containerColor: Color,
    isOffline: Boolean,
    shouldShowNetworkStatusIndicator: Boolean,
    bottomNavigationContainerColor: Color,
    bottomNavigationIconsColor: Color
) {
    val context = LocalContext.current

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
        topBar = {
            NetworkConnectivity(
                isOffline = isOffline,
                shouldShowNetworkStatusIndicator = shouldShowNetworkStatusIndicator
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.pointerInput(Unit) {},
                containerColor = bottomNavigationContainerColor,
                contentColor = Color.Transparent
            ) {
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
                            indicatorColor = Color.Transparent,
                            selectedIconColor = bottomNavigationIconsColor,
                            unselectedIconColor = Color.Gray
                        )
                    )
                }
            }
        },
        containerColor = containerColor
    ) { innerPadding ->
        NavHost(
            modifier = Modifier
                .topPadding(top = innerPadding.calculateTopPadding() - 10.dp)
                .topPadding(if (context.hasCameraNotch()) 10.dp else 0.dp)
                .horizontalPadding(
                    horizontal = innerPadding.calculateStartPadding(LayoutDirection.Ltr) +
                            innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                )
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

                BackHandler {
                    bottomNavController.popUpToHomeScreen()
                }

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