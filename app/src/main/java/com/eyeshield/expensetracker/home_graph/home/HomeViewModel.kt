package com.eyeshield.expensetracker.home_graph.home

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.api.ApiResult
import com.eyeshield.expensetracker.data.local.dao.CreditAccountDao
import com.eyeshield.expensetracker.data.local.entity.CreditAccount
import com.eyeshield.expensetracker.data.mapper.toEntityModelList
import com.eyeshield.expensetracker.data.mapper.toUIModelListFromDTO
import com.eyeshield.expensetracker.data.mapper.toUIModelListFromEntity
import com.eyeshield.expensetracker.data.remote.CreditAccountDTO
import com.eyeshield.expensetracker.home_graph.home.data.CardInfo
import com.eyeshield.expensetracker.home_graph.home.data.CreditAccountUIModel
import com.eyeshield.expensetracker.home_graph.home.domain.TransformCreditCards
import com.eyeshield.expensetracker.networking.post
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val client: HttpClient,
    private val transformCreditCards: TransformCreditCards,
    private val creditAccountDao: CreditAccountDao
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
            val creditAccounts = withContext(Dispatchers.IO) {
                creditAccountDao.getCreditAccounts()
            }
            if (creditAccounts.isEmpty()) {
                doCreditAccountsInfoCall()
            } else {
                // Sorts the elements with selected element at top
                updateCreditAccounts(
                    creditAccounts.sortedBy {
                        it.isSelected
                    }.toUIModelListFromEntity()
                )
                shouldShowLoader(false)
            }
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

            is UiAction.TransformCreditCards -> {
                transformCreditCards.getTransformedCardsForSelectedIndex(
                    cardInfoList = _uiState.value.cardInfoList,
                    index = action.index,
                    selectedCard = action.selectedCard
                )
                // To Trigger state update in UI
                viewModelScope.launch {
                    updateSelectedCreditAccount(action.id)
                }
                updateCardInfoListAndSelectedCard(action.selectedCard)
            }
        }
    }

    /**
     * Updates the selected card isSelected to true in DB and other cards isSelected to False in DB
     * **/
    private suspend fun updateSelectedCreditAccount(id: String) {
        withContext(Dispatchers.IO) {
            creditAccountDao.updateSelectedCreditAccount(id)
            creditAccountDao.updateUnSelectedCreditAccount(id)
        }
    }

    private fun updateCardInfoListAndSelectedCard(selectedCard: CardInfo) {
        _uiState.update {
            it.copy(
                cardInfoList = _uiState.value.cardInfoList,
                selectedCard = selectedCard.copy()
            )
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

        val result = client.post<Any, List<CreditAccountDTO>>(
            route = "/accounts/credit_account_info"
        )

        when (result) {

            is ApiResult.Success -> {
                updateCreditAccounts(result.data.toUIModelListFromDTO())
                insertCreditAccounts(result.data.toEntityModelList())
            }

            is ApiResult.ApiError -> {
                updateToastMessageAndVisibility(result.error.message)
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

    private fun updateCreditAccounts(data: List<CreditAccountUIModel>) {
        _uiState.update {
            it.copy(
                creditAccounts = data
            )
        }
    }

    private suspend fun insertCreditAccounts(creditAccounts: List<CreditAccount>) {
        withContext(Dispatchers.IO) {
            creditAccountDao.insertCreditAccount(
                creditAccount = creditAccounts.toTypedArray()
            )
        }
    }

    /**
     * Regardless of more than 3 cards we will be limiting the display of cards in UI to 3 cards.
     * If 1 cards we will be showing only one cards or else none if its 0
     * **/
    data class UiState(
        val isLoading: Boolean = true,
        val creditAccounts: List<CreditAccountUIModel> = listOf(),
        val shouldShowToast: Boolean = false,
        val errorMessage: String = "",
        val isPullToRefreshInProgress: Boolean = false,
        val selectedCard: CardInfo = CardInfo(),
        val cardInfoList: MutableList<CardInfo> = mutableListOf(
            CardInfo(
                position = 0,
                zIndex = 0f,
                offsetY = 0.dp,
                cardColor = R.color.card_color_1
            ),
            CardInfo(
                position = 1,
                zIndex = 1f,
                offsetY = 30.dp,
                cardColor = R.color.card_color_2
            ),
            CardInfo(
                position = 2,
                zIndex = 2f,
                offsetY = 60.dp,
                cardColor = R.color.card_color_3
            )
        )
    )

    sealed interface UiAction {
        data object OnTrackIndicatorFinished : UiAction
        data object OnPullToRefreshClicked : UiAction

        /** Action To Update Cards Position when user chooses a particular card **/
        data class TransformCreditCards(
            val id: String,
            val index: Int,
            val selectedCard: CardInfo
        ) : UiAction
    }
}