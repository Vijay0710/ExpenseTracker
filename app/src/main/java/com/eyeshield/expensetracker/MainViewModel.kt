package com.eyeshield.expensetracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyeshield.expensetracker.api.ApiResult
import com.eyeshield.expensetracker.auth.AccessTokenRequest
import com.eyeshield.expensetracker.auth.AccessTokenResponse
import com.eyeshield.expensetracker.networking.post
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionStorage: EncryptedSessionStorage,
    private val client: HttpClient
) : ViewModel() {
    var state by mutableStateOf(MainAppState())
        private set

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            state = state.copy(
                isCheckingAuth = true
            )
            validateAccessToken()
        }
    }

    private suspend fun validateAccessToken() {
        val result = client.post<AccessTokenRequest, AccessTokenResponse>(
            route = "/auth/verify_token",
            body = AccessTokenRequest(
                refresh_token = sessionStorage.get()?.refreshToken.orEmpty()
            )
        )

        when (result) {
            is ApiResult.Success -> {
                state = state.copy(
                    isLoggedIn = true
                )
                _uiEvent.send(
                    UIEvent.OnVerifyTokenSuccess
                )
            }

            is ApiResult.ApiError -> {
                sessionStorage
                _uiEvent.send(
                    UIEvent.OnVerifyTokenFailure
                )
                sessionStorage.clear()
            }
        }

        state = state.copy(
            isCheckingAuth = false
        )

    }

    sealed interface UIEvent {
        data object OnVerifyTokenSuccess : UIEvent
        data object OnVerifyTokenFailure : UIEvent
    }

}