package com.eyeshield.expensetracker.auth.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyeshield.expensetracker.EncryptedSessionStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionStorage: EncryptedSessionStorage
) : ViewModel() {

    var loginState by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<UiEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onUiAction(action: UiAction) {
        when (action) {
            is UiAction.OnTogglePasswordVisibility -> {
                loginState = loginState.copy(
                    isPasswordVisible = action.state
                )
            }

            is UiAction.OnEmailValueChange -> {
                loginState = loginState.copy(
                    email = action.value,
                    isEmailValid = validateEmail(action.value)
                )
            }

            is UiAction.OnPasswordValueChange -> {
                loginState = loginState.copy(
                    password = action.value
                )
            }

            UiAction.OnLoginClick -> {
                loginState = loginState.copy(
                    shouldShowLoader = true
                )
                doLoginCall()
            }

            UiAction.OnTrackAnimationFinished -> {
                loginState = loginState.copy(
                    shouldShowToast = false
                )
            }
        }
    }

    private fun doLoginCall() {
        viewModelScope.launch {
            eventChannel.send(UiEvent.OnLoginSuccess)

            loginState = loginState.copy(
                shouldShowLoader = false
            )
        }
    }

    private fun updateErrorMessage(message: String) {
        loginState = loginState.copy(
            errorMessage = message
        )
    }

    private fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    data class LoginState(
        val email: String = "vijaymangalani4588@gmail.com",
        val isEmailValid: Boolean = false,
        val password: String = "vijay*710",
        val isPasswordVisible: Boolean = false,
        val shouldShowLoader: Boolean = false,
        val shouldShowToast: Boolean = false,
        val errorMessage: String = ""
    )

    sealed interface UiAction {
        data class OnTogglePasswordVisibility(val state: Boolean) : UiAction
        data class OnEmailValueChange(val value: String) : UiAction
        data class OnPasswordValueChange(val value: String) : UiAction
        data object OnLoginClick : UiAction
        data object OnTrackAnimationFinished : UiAction
    }

    sealed interface UiEvent {
        data object OnLoginSuccess : UiEvent
    }
}