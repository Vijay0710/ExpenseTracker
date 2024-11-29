package com.eyeshield.expensetracker.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphNavigator
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import androidx.navigation.NavigatorProvider
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator

abstract class CustomNavHostController(context: Context) : NavHostController(context) {
    init {
        navigatorProvider.addNavigator(ComposeNavGraphNavigator(navigatorProvider))
        navigatorProvider.addNavigator(ComposeNavigator())
        navigatorProvider.addNavigator(DialogNavigator())
    }
}

@Navigator.Name("navigation")
class ComposeNavGraphNavigator(navigatorProvider: NavigatorProvider) :
    NavGraphNavigator(navigatorProvider)

@Composable
inline fun <reified T : CustomNavHostController> rememberCustomNavController(
    vararg navigators: Navigator<out NavDestination>
): T {
    val context = LocalContext.current
    return rememberSaveable(
        inputs = navigators,
        saver = navControllerSaver(
            context = context,
            factory = { ctx ->
                T::class.java.getConstructor(Context::class.java).newInstance(ctx)
            }
        )
    ) {
        T::class.java.getConstructor(Context::class.java).newInstance(context)
    }.apply {
        for (navigator in navigators) {
            navigatorProvider.addNavigator(navigator)
        }
    }
}

fun <T : CustomNavHostController> navControllerSaver(
    context: Context, factory: (Context) -> T
): Saver<T, *> =
    Saver(
        save = {
            it.saveState()
        },
        restore = { savedState ->
            factory(context).apply {
                restoreState(savedState)
            }
        }
    )

