package com.eyeshield.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.eyeshield.expensetracker.bottomNav.BottomNavigation
import com.eyeshield.expensetracker.calendar_graph.expense.AddExpenseScreen
import com.eyeshield.expensetracker.common.ObserveAsEvents
import com.eyeshield.expensetracker.components.rememberCustomNavController
import com.eyeshield.expensetracker.extensions.fadeAndZoomInTransition
import com.eyeshield.expensetracker.extensions.fadeAndZoomOutTransition
import com.eyeshield.expensetracker.extensions.noEnterTransition
import com.eyeshield.expensetracker.extensions.noExitTransition
import com.eyeshield.expensetracker.extensions.slideInFromRightToLeft
import com.eyeshield.expensetracker.extensions.slideOutFromRightToLeft
import com.eyeshield.expensetracker.home_graph.statistics.StatisticsScreen
import com.eyeshield.expensetracker.welcome.WelcomeScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.ExperimentalSerializationApi
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        enableEdgeToEdge()

        setContent {

            val navController = rememberCustomNavController<ApplicationNavController>()
            var startDestination by remember { mutableStateOf<MainNavRoutes>(MainNavRoutes.AuthRoute) }

            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None }
                ) {
                    authNavGraph(navController)

                    composable<MainNavRoutes.BottomNavigation>(
                        exitTransition = fadeAndZoomOutTransition(),
                        popEnterTransition = fadeAndZoomInTransition(),
                    ) {
                        BottomNavigation(
                            mainNavController = navController,
                        )
                    }

                    composable<MainNavRoutes.StatisticsScreen>(
                        enterTransition = slideInFromRightToLeft(),
                        exitTransition = slideOutFromRightToLeft()
                    ) {
                        StatisticsScreen(navController)
                    }

                    composable<MainNavRoutes.AddExpenseScreen>(
                        enterTransition = slideInFromRightToLeft(),
                        exitTransition = slideOutFromRightToLeft()
                    ) {
                        AddExpenseScreen(navController)
                    }
                }

            }
        }
    }
}

fun NavGraphBuilder.authNavGraph(navController: ApplicationNavController) {
    navigation<MainNavRoutes.AuthRoute>(
        startDestination = AuthRoutes.WelcomeScreen
    ) {
        composable<AuthRoutes.WelcomeScreen>(
            popEnterTransition = noEnterTransition(),
            exitTransition = noExitTransition(),
        ) {
            WelcomeScreen(
                onNextClick = {
                    navController.navigateToSingleTop(AuthRoutes.LoginScreen)
                }
            )
        }

        composable<AuthRoutes.LoginScreen>(
            enterTransition = slideInFromRightToLeft(),
            popExitTransition = slideOutFromRightToLeft(),
        ) {
            val viewModel = hiltViewModel<LoginViewModel>()

            ObserveAsEvents(viewModel.events) { event ->
                when (event) {
                    LoginViewModel.UiEvent.OnLoginSuccess -> {
                        Timber.tag("VIJ").d("In login Success")
                        navController.navigateToSingleTopAndPopAllScreens(MainNavRoutes.BottomNavigation)
                    }
                }
            }

            LoginScreen(
                uiState = viewModel.loginState,
                uiAction = viewModel::onUiAction
            )
        }
    }
}