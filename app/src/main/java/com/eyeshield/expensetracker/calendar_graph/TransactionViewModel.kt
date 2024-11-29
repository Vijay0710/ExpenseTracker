package com.eyeshield.expensetracker.calendar_graph

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyeshield.expensetracker.calendar_graph.data.TransactionData
import com.eyeshield.expensetracker.dao.TransactionDao
import com.eyeshield.expensetracker.database.DatabaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class TransactionViewModel @Inject constructor(private val transactionDao: TransactionDao) :
    ViewModel() {
    var databaseResult = mutableStateOf<DatabaseResult<List<TransactionData>>?>(null)

    fun recordATransaction(item: TransactionData) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                transactionDao.recordATransaction(item)
            } catch (e: Exception) {
                coroutineContext.ensureActive()
                Log.d("VIJ07", e.toString())
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
            Log.d("VIJ07", e.toString())
            null
        }
    }

}