@file:OptIn(ExperimentalSerializationApi::class)

package com.eyeshield.expensetracker.home_graph.home.data

import androidx.annotation.DrawableRes
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.extensions.or
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import timber.log.Timber
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Serializable
data class CreditAccountResponse(
    val id: String? = null,
    @JsonNames("bank_name")
    val bankName: String? = null,
    @JsonNames("credit_card_limit")
    val creditCardLimit: String? = null,
    @JsonNames("credit_card_due_date")
    val creditCardDueDate: String? = null,
    @JsonNames("credit_card_outstanding")
    val creditCardOutStanding: Float? = null,
    @JsonNames("account_number")
    val accountNumber: String? = null,
    @JsonNames("card_type")
    val cardType: String? = null
) {
    fun formatCreditCardLimit(): String {
        return "₹ ${formatToIndianNumberingSystem(creditCardLimit?.toLong() ?: 0L)}"
    }

    fun calculateProgress(): Float {
        return ((creditCardLimit?.toFloat() ?: 0f) -
                (creditCardOutStanding ?: 0f)
                ) / (creditCardLimit?.toFloat() ?: 0f)
    }

    fun formatAccountNumberForDisplay(): String {
        Timber.tag("VIJ").d("ACCOUNT NUMBER: ${accountNumber?.chunked(4)?.joinToString(" ")}")
        return accountNumber
            ?.chunked(4)
            ?.mapIndexed { index, chunk ->
                if (index < 3) "••••" else chunk
            }
            ?.joinToString(" ")
            .or("-")
    }

    @DrawableRes
    fun getLogoType(): Int {
        return when (cardType) {
            "MASTERCARD" -> R.drawable.mastercard_logo
            else -> R.drawable.visa_logo
        }
    }

    private fun formatToIndianNumberingSystem(number: Long): String {
        val symbols = DecimalFormatSymbols(Locale("en", "IN"))
        val formatter = DecimalFormat("#,##,###.##", symbols)
        return formatter.format(number)
    }
}
