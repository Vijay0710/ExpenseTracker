package com.eyeshield.expensetracker.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyeshield.expensetracker.cards.data.CardsInfoUIModel
import com.eyeshield.expensetracker.cards.data.CreditCardInfoUIModel
import com.eyeshield.expensetracker.data.local.dao.CreditAccountDao
import com.eyeshield.expensetracker.data.local.entity.CreditAccount
import com.eyeshield.expensetracker.data.mapper.toCardInfoUIModel
import com.eyeshield.expensetracker.data.mapper.toCreditAccountInfoUIModelListFromEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val creditAccountDao: CreditAccountDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = UIState()
    )

    init {
        viewModelScope.launch {
            updateCreditAccountsInUI()
        }
    }

    fun onUiAction(action: UIAction) {
        when (action) {
            is UIAction.VisibleCard -> {
                viewModelScope.launch {
                    updateSelectedCardInfo(action.id, action.bankName)
                }
            }
        }
    }

    private suspend fun getCreditAccounts(): List<CreditAccount> {
        return withContext(Dispatchers.IO) {
            creditAccountDao.getCreditAccounts().sortedBy {
                it.isSelected
            }
        }
    }

    private suspend fun updateCreditAccountsInUI() {
        _uiState.update {
            it.copy(
                cardsList = getCreditAccounts().toCreditAccountInfoUIModelListFromEntity()
            )
        }
    }

    private suspend fun updateSelectedCardInfo(id: String, bankName: String) {
        _uiState.update { state ->
            state.copy(
                cardTitle = bankName,
                selectedCardInfo = getCreditAccounts().first { item ->
                    item.id == id
                }.toCardInfoUIModel()
            )
        }
    }

    sealed interface UIAction {
        data class VisibleCard(val id: String, val bankName: String) : UIAction
    }


    data class UIState(
        val cardTitle: String = "",
        val cardsList: List<CreditCardInfoUIModel> = listOf(),
        val selectedCardInfo: CardsInfoUIModel = CardsInfoUIModel(
            totalLimit = "",
            dueDate = "",
            lastDueDate = "",
            billingCycle = "",
            totalRewardsPoints = ""
        )
    )
}