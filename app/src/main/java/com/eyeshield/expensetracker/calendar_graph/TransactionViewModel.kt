package com.eyeshield.expensetracker.calendar_graph

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyeshield.expensetracker.calendar.CustomCalendar
import com.eyeshield.expensetracker.calendar_graph.data.CalendarData
import com.eyeshield.expensetracker.data.local.dao.TransactionDao
import com.eyeshield.expensetracker.data.local.database.DatabaseResult
import com.eyeshield.expensetracker.data.local.entity.TransactionData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionDao: TransactionDao,
    private val customCalendar: CustomCalendar
) : ViewModel() {
    var databaseResult = mutableStateOf<DatabaseResult<List<TransactionData>>?>(null)

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        updateCalendarData()
    }

    fun onUiAction(action: UiAction) {
        when (action) {
            is UiAction.OnLeftChevronClicked -> {
                customCalendar.monthIndex = action.currentMonthPosition - 1
                Timber.tag("VIJ")
                    .d("Left Chevron Month Index After Updating is: ${customCalendar.monthIndex}")
                updateMonthAndYear()
            }

            is UiAction.OnRightChevronClicked -> {
                customCalendar.monthIndex = action.currentMonthPosition + 1
                Timber.tag("VIJ")
                    .d("Right Chevron Month Index After Updating is: ${customCalendar.monthIndex}")
                updateMonthAndYear()
            }

            is UiAction.UpdateSelectedDay -> {
                updateSelectedDay(day = action.selectedDay)
            }
        }
    }

    private fun updateSelectedDay(day: Int) {
        _uiState.update {
            it.copy(
                calendarData = it.calendarData.copy(
                    selectedDay = day
                )
            )
        }
    }

    private fun updateMonthAndYear() {
        _uiState.update {
            it.copy(
                calendarData = it.calendarData.copy(
                    monthIndex = customCalendar.monthIndex,
                    monthAndYear = customCalendar.getMonthAndYear(),
                    dayStartingColumn = customCalendar.getIndexOfWeekForStartDayOfCurrentMonth() - 1,
                    currentDay = customCalendar.getCurrentDay(),
                    totalDays = customCalendar.getTotalDaysForMonth(),
                    selectedDay = 0
                )
            )
        }

        Timber.tag("VIJ").d("Month and Year After Updating is: ${customCalendar.getMonthAndYear()}")
    }

    private fun updateCalendarData() {
        _uiState.update {
            it.copy(
                calendarData = CalendarData(
                    monthIndex = customCalendar.monthIndex,
                    monthAndYear = customCalendar.getMonthAndYear(),
                    dayStartingColumn = customCalendar.getIndexOfWeekForStartDayOfCurrentMonth() - 1,
                    currentDay = customCalendar.getCurrentDay(),
                    totalDays = customCalendar.getTotalDaysForMonth()
                )
            )
        }
    }

    fun recordATransaction(item: TransactionData) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                transactionDao.recordATransaction(item)
            } catch (e: Exception) {
                coroutineContext.ensureActive()
            }
        }
    }

    suspend fun getTransactions(): List<TransactionData>? {
        return try {
            databaseResult.value = DatabaseResult.Loading()
            withContext(Dispatchers.IO) {
                delay(2000)
                val transactions = transactionDao.getAllTransactions()
                databaseResult.value = DatabaseResult.Success(
                    transactionDao.getAllTransactions()
                )
                transactions
            }
        } catch (e: Exception) {
            // Propagates Cancellation exception if any to upwards
            coroutineContext.ensureActive()
            databaseResult.value = DatabaseResult.Error(e)
            Timber.tag("VIJ07").d(e.toString())
            null
        }
    }

    sealed interface UiAction {
        data class OnRightChevronClicked(val currentMonthPosition: Int) : UiAction
        data class OnLeftChevronClicked(val currentMonthPosition: Int) : UiAction
        data class UpdateSelectedDay(val selectedDay: Int) : UiAction
    }

    data class UiState(
        val calendarData: CalendarData = CalendarData()
    )

}