package com.eyeshield.expensetracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyeshield.expensetracker.api.ApiResult
import com.eyeshield.expensetracker.auth.data.AccessTokenRequest
import com.eyeshield.expensetracker.auth.data.AccessTokenResponse
import com.eyeshield.expensetracker.networking.connectivity.ConnectivityObserver
import com.eyeshield.expensetracker.networking.post
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionStorage: EncryptedSessionStorage,
    private val client: HttpClient,
    private val connectivityObserver: ConnectivityObserver,
    private val apiScope: CoroutineScope
) : ViewModel() {
    var state by mutableStateOf(MainAppState())
        private set

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            updateIsCheckingAuth(true)
            // Should only collect only when the user is not logged in or else on each network connection we would be doing API call

            connectivityObserver.observe().collect { status ->
                when (status) {
                    ConnectivityObserver.Status.AVAILABLE -> {
                        // Where there is no data in sessionStorage avoid validateRefreshToken call and instead we tell user to login again
                        if (sessionStorage.get() != null) {
                            if (!state.isLoggedIn) {
                                apiScope.launch {
                                    validateRefreshToken()
                                }
                            }
                        } else {
                            updateIsCheckingAuth(false)
                            handleVerifyTokenFailure()
                        }
                    }

                    ConnectivityObserver.Status.LOST,
                    ConnectivityObserver.Status.UNAVAILABLE,
                    ConnectivityObserver.Status.LOSING -> {
                        if (sessionStorage.get() == null) {
                            updateIsCheckingAuth(false)
                            handleVerifyTokenFailure()
                        } else {
                            apiScope.coroutineContext.cancelChildren()
                            _uiEvent.send(UIEvent.OnOfflineAccess)
                            updateIsCheckingAuth(false)
                            updateLoginInStatus(true)
                        }
                    }
                }
            }
        }
    }

    private fun updateIsCheckingAuth(isChecking: Boolean) {
        state = state.copy(
            isCheckingAuth = isChecking
        )
    }

    private fun updateLoginInStatus(isLoggedIn: Boolean) {
        state = state.copy(
            isLoggedIn = isLoggedIn
        )
    }

    private suspend fun validateRefreshToken() {
        val result = client.post<AccessTokenRequest, AccessTokenResponse>(
            route = "/auth/verify_token",
            body = AccessTokenRequest(
                refresh_token = sessionStorage.get()?.refreshToken.orEmpty()
            )
        )

        withContext(Dispatchers.Main) {
            when (result) {
                is ApiResult.Success -> {
                    updateLoginInStatus(true)
                    _uiEvent.send(UIEvent.OnVerifyTokenSuccess)
                }

                is ApiResult.ApiError -> {
                    handleVerifyTokenFailure()
                }
            }
            updateIsCheckingAuth(false)
        }

    }

    private suspend fun handleVerifyTokenFailure() {
        _uiEvent.send(UIEvent.OnVerifyTokenFailure)
        sessionStorage.clear()
    }

    sealed interface UIEvent {
        data object OnVerifyTokenSuccess : UIEvent
        data object OnVerifyTokenFailure : UIEvent
        data object OnOfflineAccess : UIEvent
    }

}