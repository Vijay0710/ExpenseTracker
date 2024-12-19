package com.eyeshield.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.eyeshield.expensetracker.extensions.fadeAndZoomInTransition
import com.eyeshield.expensetracker.extensions.fadeAndZoomOutTransition
import com.eyeshield.expensetracker.extensions.slideInFromRightToLeft
import com.eyeshield.expensetracker.extensions.slideOutFromRightToLeft
import com.eyeshield.expensetracker.home_graph.statistics.StatisticsScreen
import com.eyeshield.expensetracker.utils.setStatusBarIconsColorToDark
import com.eyeshield.expensetracker.welcome.WelcomeScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.ExperimentalSerializationApi

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.isCheckingAuth
            }
        }

        enableEdgeToEdge()
        setStatusBarIconsColorToDark(false)

        setContent {

            val navController = rememberCustomNavController<ApplicationNavController>()
            var startDestination by remember { mutableStateOf<MainNavRoutes>(MainNavRoutes.AuthRoute) }
            var surfaceBackGround by remember { mutableIntStateOf(R.color.login_screen_background) }

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

            navController.addOnDestinationChangedListener { _, navDestination, _ ->
                surfaceBackGround = when (navDestination.route) {
                    MainNavRoutes.BottomNavigation.serializer().descriptor.serialName,
                    AuthRoutes.WelcomeScreen.serializer().descriptor.serialName,
                    AuthRoutes.LoginScreen.serializer().descriptor.serialName -> {
                        R.color.login_screen_background
                    }

                    else -> {
                        R.color.shadow_white
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = colorResource(id = surfaceBackGround)
            ) {
                if (!viewModel.state.isCheckingAuth) {
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
                            LaunchedEffect(Unit) {
                                setStatusBarIconsColorToDark(true)
                            }

                            BottomNavigation(navController)
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
                            LaunchedEffect(Unit) {
                                setStatusBarIconsColorToDark(false)
                            }
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
        startDestination = AuthRoutes.WelcomeScreen
    ) {
        composable<AuthRoutes.WelcomeScreen>(
            popEnterTransition = fadeAndZoomInTransition(),
            exitTransition = fadeAndZoomOutTransition(),
        ) {
            WelcomeScreen(
                onNextClick = {
                    navController.navigateToSingleTop(AuthRoutes.LoginScreen)
                }
            )
        }

        composable<AuthRoutes.LoginScreen>(
            enterTransition = slideInFromRightToLeft(),
            popExitTransition = slideOutFromRightToLeft()
        ) {
            val viewModel = hiltViewModel<LoginViewModel>()

            ObserveAsEvents(viewModel.events) { event ->
                when (event) {
                    LoginViewModel.UiEvent.OnLoginSuccess -> {
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