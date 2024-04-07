package com.eyeshield.expensetracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eyeshield.expensetracker.calendar.models.TransactionData
import com.eyeshield.expensetracker.dao.TransactionDao

@Database(entities = [TransactionData::class], version = 1)
abstract class ExpenseTrackDatabase : RoomDatabase() {
    abstract fun TransactionDao(): TransactionDao
}