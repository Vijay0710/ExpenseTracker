@file:OptIn(ExperimentalSerializationApi::class)

package com.eyeshield.expensetracker.home_graph.home.data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Serializable
data class CreditAccountResponseModel(
    val id: String? = null,
    @JsonNames("bank_name")
    val bankName: String? = null,
    @JsonNames("credit_card_limit")
    val creditCardLimit: String? = null,
    @JsonNames("credit_card_due_date")
    val creditCardDueDate: String? = null,
    @JsonNames("credit_card_outstanding")
    val creditCardOutStanding: Float? = null
) {
    fun formatCreditCardLimit(): String {
        return "₹ ${formatToIndianNumberingSystem(creditCardLimit?.toLong() ?: 0L)}"
    }

    fun calculateProgress(): Float {
        return ((creditCardLimit?.toFloat() ?: 0f) -
                (creditCardOutStanding ?: 0f)
                ) / (creditCardLimit?.toFloat() ?: 0f)
    }

    private fun formatToIndianNumberingSystem(number: Long): String {
        val symbols = DecimalFormatSymbols(Locale("en", "IN"))
        val formatter = DecimalFormat("#,##,###.##", symbols)
        return formatter.format(number)
    }
}
