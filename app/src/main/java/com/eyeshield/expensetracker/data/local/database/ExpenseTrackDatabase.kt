package com.eyeshield.expensetracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eyeshield.expensetracker.data.local.dao.CreditAccountDao
import com.eyeshield.expensetracker.data.local.dao.TransactionDao
import com.eyeshield.expensetracker.data.local.entity.CreditAccount
import com.eyeshield.expensetracker.data.local.entity.TransactionData

@Database(
    entities = [
        TransactionData::class,
        CreditAccount::class
    ],
    version = 4
)
abstract class ExpenseTrackDatabase : RoomDatabase() {

    abstract fun TransactionDao(): TransactionDao

    abstract fun CreditAccountDao(): CreditAccountDao
}