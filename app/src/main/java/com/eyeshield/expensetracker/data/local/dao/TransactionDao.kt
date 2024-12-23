package com.eyeshield.expensetracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.eyeshield.expensetracker.data.local.entity.TransactionData

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions")
    fun getAllTransactions() : List<TransactionData>

    @Insert
    suspend fun recordATransaction(transactionData: TransactionData)
}