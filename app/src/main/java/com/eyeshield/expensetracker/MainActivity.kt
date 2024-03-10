package com.eyeshield.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eyeshield.expensetracker.home_graph.compose.statistics.StatisticsScreen
import com.eyeshield.expensetracker.ui.theme.ExpenseTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            ExpenseTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(id = R.color.shadow_white)
                ) {
                    NavHost(
                        navController = navController,
                        startDestination =  MainNavRoutes.BottomNavigation.route
                    ) {
                        composable(MainNavRoutes.BottomNavigation.route) {
                            BottomNavigation(navController)
                        }

                        composable(
                            MainNavRoutes.StatisticsScreen.route,
                            enterTransition = NavigationExtensions.slideIntoContainerFromRightToLeft(),
                            exitTransition = NavigationExtensions.slideIntoContainerFromLeftToRight()
                        ) {
                            StatisticsScreen(navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExpenseTrackerTheme {
        Greeting("Android")
    }
}