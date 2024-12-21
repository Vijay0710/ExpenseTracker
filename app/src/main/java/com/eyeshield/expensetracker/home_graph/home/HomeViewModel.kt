package com.eyeshield.expensetracker.home_graph.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyeshield.expensetracker.api.ApiResult
import com.eyeshield.expensetracker.api.DataError
import com.eyeshield.expensetracker.home_graph.home.data.CreditAccountResponse
import com.eyeshield.expensetracker.networking.post
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val client: HttpClient
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        UiState()
    )

    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UiState()
    )

    init {
        viewModelScope.launch {
            doCreditAccountsInfoCall()
        }
    }

    fun onUiAction(action: UiAction) {
        when (action) {
            UiAction.OnTrackIndicatorFinished -> {
                updateToastVisibilityState(false)
            }

            UiAction.OnPullToRefreshClicked -> {
                updatePullToRefreshStatus(true)
                viewModelScope.launch {
                    doCreditAccountsInfoCall(
                        isPullToRefresh = true
                    )
                }
            }
        }
    }

    private fun updateToastVisibilityState(visible: Boolean) {
        _uiState.update {
            it.copy(
                shouldShowToast = visible
            )
        }
    }

    private fun shouldShowLoader(shouldShow: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = shouldShow
            )
        }
    }

    private suspend fun doCreditAccountsInfoCall(
        isPullToRefresh: Boolean = false
    ) {
        shouldShowLoader(true)

        val result = client.post<Any, List<CreditAccountResponse>>(
            route = "/accounts/credit_account_info"
        )

        when (result) {

            is ApiResult.Success -> {
                updateCreditAccounts(
                    result.data.subList(
                        0, result.data.size.coerceIn(1, 3)
                    )
                )
            }

            is ApiResult.ApiError -> {
                when (result.error) {
                    DataError.Network.REQUEST_TIMED_OUT -> {
                        updateToastMessageAndVisibility(
                            message = "Oops! Your request took too long to respond. " +
                                    "Can you check your intenet connection and try again later",
                        )
                    }

                    DataError.Network.NO_INTERNET -> {
                        updateToastMessageAndVisibility(
                            message = "Looks like we’ve lost connection to the internet! " +
                                    "Kindly ensure you're connected and give it another shot.",
                        )
                    }

                    else -> {
                        updateToastMessageAndVisibility(
                            message = "Something went wrong on our end! Our team is on it, " +
                                    "armed with coffee and determination!",
                        )
                    }
                }
            }
        }

        shouldShowLoader(false)

        if (isPullToRefresh) {
            updatePullToRefreshStatus(status = false)
        }
    }

    private fun updatePullToRefreshStatus(status: Boolean) {
        _uiState.update {
            it.copy(
                isPullToRefreshInProgress = status
            )
        }
    }

    private fun updateToastMessageAndVisibility(message: String) {
        _uiState.update {
            it.copy(
                errorMessage = message
            )
        }
        updateToastVisibilityState(true)
    }

    private fun updateCreditAccounts(data: List<CreditAccountResponse>) {
        _uiState.update {
            it.copy(
                creditAccounts = data
            )
        }
    }


    data class UiState(
        val isLoading: Boolean = true,
        val creditAccounts: List<CreditAccountResponse> = listOf(),
        val shouldShowToast: Boolean = false,
        val errorMessage: String = "",
        val isPullToRefreshInProgress: Boolean = false
    )

    sealed interface UiAction {
        data object OnTrackIndicatorFinished : UiAction
        data object OnPullToRefreshClicked : UiAction
    }
}