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
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eyeshield.expensetracker.application.ApplicationNavController
import com.eyeshield.expensetracker.application.Routes
import com.eyeshield.expensetracker.bottomNav.BottomNavigation
import com.eyeshield.expensetracker.calendar_graph.expense.AddExpenseScreen
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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.shouldShowSplashScreen
            }
        }

        enableEdgeToEdge()

        setContent {

            val navController = rememberCustomNavController<ApplicationNavController>()

            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                viewModel.startDestination?.let {
                    NavHost(
                        navController = navController,
                        startDestination = viewModel.startDestination!!,
                        exitTransition = { ExitTransition.None },
                        popEnterTransition = { EnterTransition.None }
                    ) {
                        composable<Routes.WelcomeScreen>(
                            popEnterTransition = noEnterTransition(),
                            exitTransition = noExitTransition(),
                        ) {
                            WelcomeScreen(
                                onNextClick = {
                                    viewModel.hideWelcomeScreenOnNextAppLaunch()
                                    navController.navigateToSingleTopAndPopAllScreens(Routes.BottomNavigation)
                                }
                            )
                        }

                        composable<Routes.BottomNavigation>(
                            exitTransition = fadeAndZoomOutTransition(),
                            popEnterTransition = fadeAndZoomInTransition(),
                        ) {
                            BottomNavigation(
                                mainNavController = navController,
                            )
                        }

                        composable<Routes.StatisticsScreen>(
                            enterTransition = slideInFromRightToLeft(),
                            exitTransition = slideOutFromRightToLeft()
                        ) {
                            StatisticsScreen(navController)
                        }

                        composable<Routes.AddExpenseScreen>(
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
}
