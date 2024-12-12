package com.eyeshield.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.eyeshield.expensetracker.application.ApplicationNavController
import com.eyeshield.expensetracker.application.AuthRoutes
import com.eyeshield.expensetracker.application.MainNavRoutes
import com.eyeshield.expensetracker.auth.login.LoginScreen
import com.eyeshield.expensetracker.auth.login.LoginViewModel
import com.eyeshield.expensetracker.bottomnav.BottomNavigation
import com.eyeshield.expensetracker.calendar_graph.expense.AddExpenseScreen
import com.eyeshield.expensetracker.common.ObserveAsEvents
import com.eyeshield.expensetracker.components.rememberCustomNavController
import com.eyeshield.expensetracker.extensions.fadeOutExitTransition
import com.eyeshield.expensetracker.extensions.noExitTransition
import com.eyeshield.expensetracker.extensions.slideIntoContainerFromLeftToRight
import com.eyeshield.expensetracker.extensions.slideIntoContainerFromRightToLeft
import com.eyeshield.expensetracker.extensions.slideOutOfContainerFromRightToLeft
import com.eyeshield.expensetracker.home_graph.statistics.StatisticsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.isCheckingAuth
            }
        }

        setContent {

            val navController = rememberCustomNavController<ApplicationNavController>()
            var startDestination by remember { mutableStateOf<MainNavRoutes>(MainNavRoutes.AuthRoute) }

            ObserveAsEvents(viewModel.uiEvent) { event ->
                startDestination = when (event) {
                    MainViewModel.UIEvent.OnVerifyTokenFailure -> {
                        MainNavRoutes.AuthRoute
                    }

                    MainViewModel.UIEvent.OnVerifyTokenSuccess -> {
                        MainNavRoutes.BottomNavigation
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = colorResource(id = R.color.shadow_white)
            ) {
                if (!viewModel.state.isCheckingAuth) {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {

                        authNavGraph(navController)

                        composable<MainNavRoutes.BottomNavigation>(
                            exitTransition = fadeOutExitTransition()
                        ) {
                            BottomNavigation(navController)
                        }

                        composable<MainNavRoutes.StatisticsScreen>(
                            enterTransition = slideIntoContainerFromRightToLeft(),
                            exitTransition = slideIntoContainerFromLeftToRight()
                        ) {
                            StatisticsScreen(navController)
                        }

                        composable<MainNavRoutes.AddExpenseScreen>(
                            enterTransition = slideIntoContainerFromRightToLeft(),
                            exitTransition = slideIntoContainerFromLeftToRight()
                        ) {
                            AddExpenseScreen(navController)
                        }
                    }
                }
            }
        }
    }
}

fun NavGraphBuilder.authNavGraph(navController: ApplicationNavController) {
    navigation<MainNavRoutes.AuthRoute>(
        startDestination = AuthRoutes.LoginScreen
    ) {
        composable<AuthRoutes.LoginScreen>(
            exitTransition = noExitTransition(),
            popExitTransition = slideOutOfContainerFromRightToLeft()
        ) {
            val viewModel = hiltViewModel<LoginViewModel>()

            LoginScreen(
                uiState = viewModel.loginState,
                uiAction = viewModel::onUiAction,
                uiEvent = viewModel.events,
                onLoginSuccess = {
                    navController.navigateToSingleTopAndPopAllScreens(MainNavRoutes.BottomNavigation)
                },
                onLoginFailure = {
                    //To be handled
                }
            )
        }
    }
}