package com.eyeshield.expensetracker.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.eyeshield.expensetracker.calendar_graph.data.TransactionData

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions")
    fun getAllTransactions() : List<TransactionData>

    @Insert
    suspend fun recordATransaction(transactionData: TransactionData)
}