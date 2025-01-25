package com.eyeshield.expensetracker.cards.data

import androidx.annotation.DrawableRes

data class CreditCardInfoUIModel(
    val id: String,
    val creditCardOutStanding: String,
    val cardLimit: String,
    val progress: Float,
    val accountNumber: String,
    @DrawableRes
    val logo: Int,
    val bankName: String
)
