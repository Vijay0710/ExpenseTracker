package com.eyeshield.expensetracker.auth.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyeshield.expensetracker.EncryptedSessionStorage
import com.eyeshield.expensetracker.api.ApiResult
import com.eyeshield.expensetracker.api.DataError
import com.eyeshield.expensetracker.auth.data.AuthInfo
import com.eyeshield.expensetracker.networking.post
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.FormPart
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val client: HttpClient,
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
            val result = client.post<Any, AuthInfo>(
                route = "/auth/token",
                formPart = arrayOf(
                    FormPart("grant_type", "password"),
                    FormPart("username", loginState.email),
                    FormPart("password", loginState.password)
                )
            )

            when (result) {
                is ApiResult.Success -> {
                    sessionStorage.set(info = result.data)
                    eventChannel.send(UiEvent.OnLoginSuccess)
                }

                is ApiResult.ApiError -> {
                    loginState = loginState.copy(
                        shouldShowToast = true
                    )

                    when (result.error) {
                        DataError.Network.REQUEST_TIMED_OUT -> {
                            updateErrorMessage(
                                message = "Hmm, looks like we're taking too long to get back to you. Please give us a moment and try again shortly."
                            )
                        }

                        DataError.Network.UNAUTHORIZED -> {
                            updateErrorMessage("Oops! Your credentials seem to have taken a little vacation. Please check them and try again.")
                        }

                        DataError.Network.NO_INTERNET -> {
                            updateErrorMessage(
                                message = "Looks like we’ve lost connection to the internet! Kindly ensure you're connected and give it another shot."
                            )
                        }

                        else -> {
                            updateErrorMessage(
                                message = "Something went wrong on our end! Our team is on it, armed with coffee and determination!"
                            )
                        }
                    }
                }
            }

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