package com.eyeshield.expensetracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyeshield.expensetracker.application.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val storage: EncryptedStorage
) : ViewModel() {

    var shouldShowSplashScreen by mutableStateOf(false)
    var startDestination by mutableStateOf<Routes?>(null)

    init {
        viewModelScope.launch {

            shouldShowSplashScreen = true

            val shouldHideWelcomeScreen = storage.get(
                WELCOME_SCREEN_KEY,
                Boolean.serializer()
            )

            startDestination = if (shouldHideWelcomeScreen != null && shouldHideWelcomeScreen) {
                Routes.BottomNavigation
            } else {
                Routes.WelcomeScreen
            }

            shouldShowSplashScreen = false
        }
    }

    fun hideWelcomeScreenOnNextAppLaunch() {
        viewModelScope.launch {
            storage.set(
                key = WELCOME_SCREEN_KEY,
                serializer = Boolean.serializer(),
                info = true
            )
        }
    }

    companion object {
        private const val WELCOME_SCREEN_KEY = "welcome_screen"
    }
}