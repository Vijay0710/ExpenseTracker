package com.eyeshield.expensetracker.calendar_graph

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyeshield.expensetracker.calendar_graph.data.TransactionData
import com.eyeshield.expensetracker.dao.TransactionDao
import com.eyeshield.expensetracker.database.DatabaseResult
import com.eyeshield.expensetracker.database.DatabaseStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(private val transactionDao: TransactionDao) :
    ViewModel() {

    var databaseStatus = mutableStateOf(DatabaseStatus.SUCCESS)
    var databaseResult = mutableStateOf(transactionResult(listOf()))
    fun recordATransaction(item: TransactionData) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                transactionDao.recordATransaction(item)
            } catch (e: Exception) {
                Log.d("VIJ07", e.toString())
            }
        }
    }

    suspend fun getTransactions(): List<TransactionData>? {
        return try {
            databaseStatus.value = DatabaseStatus.LOADING
            withContext(Dispatchers.IO) {
                delay(2000)
                databaseResult.value = DatabaseResult.Success(transactionDao.getAllTransactions())
                databaseStatus.value = DatabaseStatus.SUCCESS
                databaseResult.value.data
            }
        } catch (e: Exception) {
            databaseStatus.value = DatabaseStatus.ERROR
            Log.d("VIJ07", e.toString())
            null
        }
    }

}

typealias transactionResult = DatabaseResult.Success<List<TransactionData>>