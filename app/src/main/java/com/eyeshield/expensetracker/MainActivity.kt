package com.eyeshield.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import com.eyeshield.expensetracker.bottomNav.BottomTabNavController
import com.eyeshield.expensetracker.bottomNav.Tabs
import com.eyeshield.expensetracker.calendar_graph.expense.AddExpenseScreen
import com.eyeshield.expensetracker.common.ObserveAsEvents
import com.eyeshield.expensetracker.components.rememberCustomNavController
import com.eyeshield.expensetracker.extensions.fadeAndZoomInTransition
import com.eyeshield.expensetracker.extensions.fadeAndZoomOutTransition
import com.eyeshield.expensetracker.extensions.slideInFromRightToLeft
import com.eyeshield.expensetracker.extensions.slideOutFromRightToLeft
import com.eyeshield.expensetracker.home_graph.statistics.StatisticsScreen
import com.eyeshield.expensetracker.networking.connectivity.ConnectivityObserver
import com.eyeshield.expensetracker.utils.setStatusBarIconsColorToDark
import com.eyeshield.expensetracker.welcome.WelcomeScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver

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
            val bottomNavController = rememberCustomNavController<BottomTabNavController>()
            var startDestination by remember { mutableStateOf<MainNavRoutes>(MainNavRoutes.AuthRoute) }
            var surfaceBackGround by remember { mutableIntStateOf(R.color.login_screen_background) }

            var bottomNavigationContainerColor by remember { mutableIntStateOf(R.color.login_screen_background) }
            var bottomNavigationIconsColor by remember { mutableIntStateOf(R.color.black) }

            val bottomNavigationContainerColorAnimation by animateColorAsState(
                targetValue = colorResource(bottomNavigationContainerColor),
                label = "Bottom Navigation Container Transition Animation",
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )

            val bottomNavigationIconColorAnimation by animateColorAsState(
                targetValue = colorResource(bottomNavigationIconsColor),
                label = "Bottom Navigation Container Transition Animation",
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )

            val surfaceBackGroundColorAnimation by animateColorAsState(
                targetValue = colorResource(surfaceBackGround),
                label = "Surface Background Transition Animation",
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )

            var isOffline by remember { mutableStateOf(false) }
            var shouldShowNetworkStatusIndicator by remember { mutableStateOf(false) }

            val coroutineScope = rememberCoroutineScope()
            val owner = LocalLifecycleOwner.current

            LaunchedEffect(owner) {
                lifecycleScope.launch {
                    owner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        connectivityObserver.observe().collectLatest { status ->
                            val isConnected = (status == ConnectivityObserver.Status.AVAILABLE)
                            coroutineScope.launch {
                                // If network is lost midway and it becomes available we will show Online Indicator for 3 seconds
                                if (isOffline && isConnected) {
                                    coroutineScope.launch {
                                        shouldShowNetworkStatusIndicator = true
                                        delay(3000)
                                        shouldShowNetworkStatusIndicator = false
                                    }
                                } else if (!isConnected) shouldShowNetworkStatusIndicator = true
                                // If no network available on App Start Up we will show the indicator
                                isOffline = (status != ConnectivityObserver.Status.AVAILABLE)
                            }
                        }
                    }
                }
            }

            ObserveAsEvents(viewModel.uiEvent) { event ->
                startDestination = when (event) {
                    MainViewModel.UIEvent.OnVerifyTokenFailure -> {
                        MainNavRoutes.AuthRoute
                    }

                    MainViewModel.UIEvent.OnVerifyTokenSuccess -> {
                        MainNavRoutes.BottomNavigation
                    }

                    MainViewModel.UIEvent.OnOfflineAccess -> {
                        MainNavRoutes.BottomNavigation
                    }
                }
            }

            LaunchedEffect(Unit) {
                navController.addOnDestinationChangedListener { _, navDestination, _ ->
                    surfaceBackGround = when (navDestination.route) {
                        AuthRoutes.WelcomeScreen.serializer().descriptor.serialName,
                        AuthRoutes.LoginScreen.serializer().descriptor.serialName -> {
                            R.color.login_screen_background
                        }

                        MainNavRoutes.BottomNavigation.serializer().descriptor.serialName -> {
                            R.color.shadow_white
                        }

                        else -> {
                            R.color.shadow_white
                        }
                    }
                }

                bottomNavController.addOnDestinationChangedListener { _, navDestination, _ ->
                    surfaceBackGround = when (navDestination.route) {
                        Tabs.CardScreen.serializer().descriptor.serialName -> {
                            bottomNavigationIconsColor = R.color.white
                            bottomNavigationContainerColor = R.color.login_screen_background
                            R.color.card_screen_background
                        }

                        else -> {
                            bottomNavigationIconsColor = R.color.black
                            bottomNavigationContainerColor = R.color.shadow_white
                            R.color.shadow_white
                        }
                    }
                }
            }


            Surface(
                modifier = Modifier.fillMaxSize(),
                color = surfaceBackGroundColorAnimation
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
                            SideEffect {
                                setStatusBarIconsColorToDark(true)
                            }

                            BottomNavigation(
                                mainNavController = navController,
                                bottomNavController = bottomNavController,
                                isOffline = isOffline,
                                shouldShowNetworkStatusIndicator = shouldShowNetworkStatusIndicator,
                                containerColor = surfaceBackGroundColorAnimation,
                                bottomNavigationContainerColor = bottomNavigationContainerColorAnimation,
                                bottomNavigationIconsColor = bottomNavigationIconColorAnimation,
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
                            SideEffect {
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