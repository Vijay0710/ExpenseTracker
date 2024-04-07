package com.eyeshield.expensetracker.calendar.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionData(
    @PrimaryKey
    var expenseId: String,
    var expenseResourceID: Int = 0,
    var expenseName: String? = null,
    var expenseDate: String? = null,
    var expenseAmount: String? = null
)
