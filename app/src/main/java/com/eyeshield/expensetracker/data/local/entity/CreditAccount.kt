package com.eyeshield.expensetracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "credit_account")
data class CreditAccount(
    @PrimaryKey
    val id: String,
    val bankName: String? = null,
    val creditCardLimit: String? = null,
    val creditCardDueDate: String? = null,
    val creditCardOutStanding: Float? = null,
    val accountNumber: String? = null,
    val cardType: String? = null,
    val isSelected: Boolean = false
)
