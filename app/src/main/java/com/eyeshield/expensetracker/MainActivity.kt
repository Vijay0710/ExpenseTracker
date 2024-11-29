package com.eyeshield.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eyeshield.expensetracker.application.ApplicationNavController
import com.eyeshield.expensetracker.application.MainNavRoutes
import com.eyeshield.expensetracker.bottomnav.BottomNavigation
import com.eyeshield.expensetracker.bottomnav.NavigationExtensions
import com.eyeshield.expensetracker.calendar_graph.expense.AddExpenseScreen
import com.eyeshield.expensetracker.components.rememberCustomNavController
import com.eyeshield.expensetracker.home_graph.statistics.StatisticsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            rememberNavController()
            val navController = rememberCustomNavController<ApplicationNavController>()

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = colorResource(id = R.color.shadow_white)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = MainNavRoutes.BottomNavigation
                ) {
                    composable<MainNavRoutes.BottomNavigation> {
                        BottomNavigation(navController)
                    }

                    composable<MainNavRoutes.StatisticsScreen>(
                        enterTransition = NavigationExtensions.slideIntoContainerFromRightToLeft(),
                        exitTransition = NavigationExtensions.slideIntoContainerFromLeftToRight()
                    ) {
                        StatisticsScreen(navController)
                    }

                    composable<MainNavRoutes.AddExpenseScreen>(
                        enterTransition = NavigationExtensions.slideIntoContainerFromRightToLeft(),
                        exitTransition = NavigationExtensions.slideIntoContainerFromLeftToRight()
                    ) {
                        AddExpenseScreen(navController)
                    }
                }
            }
        }
    }
}