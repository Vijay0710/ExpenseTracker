package com.eyeshield.expensetracker.home_graph.home.data

import androidx.annotation.DrawableRes

data class CreditAccountUIModel(
    val id: String,
    val creditCardOutStanding: String,
    val cardLimit: String,
    val progress: Float,
    val accountNumber: String,
    @DrawableRes
    val logo: Int,
)
