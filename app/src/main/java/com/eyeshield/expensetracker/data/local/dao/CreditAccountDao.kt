package com.eyeshield.expensetracker.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.eyeshield.expensetracker.data.local.entity.CreditAccount

@Dao
interface CreditAccountDao {

    @Query("SELECT * FROM credit_account")
    suspend fun getCreditAccounts(): List<CreditAccount>

    @Upsert
    suspend fun insertCreditAccount(vararg creditAccount: CreditAccount)

    @Query("UPDATE credit_account SET isSelected = 1 WHERE id = :id")
    suspend fun updateSelectedCreditAccount(id: String): Int

    @Query("UPDATE credit_account SET isSelected = 0 WHERE id != :id")
    suspend fun updateUnSelectedCreditAccount(id: String): Int
}