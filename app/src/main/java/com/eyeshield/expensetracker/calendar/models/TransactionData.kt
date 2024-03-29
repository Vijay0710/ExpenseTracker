package com.eyeshield.expensetracker.calendar.models

data class TransactionData(
    var expenseId: String,
    var expenseResourceID: Int,
    var expenseName: String,
    var expenseDate: String,
    var expenseAmount: String
)
