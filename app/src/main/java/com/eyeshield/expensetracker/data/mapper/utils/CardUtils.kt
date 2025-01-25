package com.eyeshield.expensetracker.data.mapper.utils

import androidx.annotation.DrawableRes
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.extensions.or
import timber.log.Timber
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object CardUtils {
    fun formatToIndianNumberingSystem(number: Long): String {
        val symbols = DecimalFormatSymbols(Locale("en", "IN"))
        val formatter = DecimalFormat("#,##,###.##", symbols)
        return formatter.format(number)
    }

    fun formatBillingCycle(billingCycle: String?): String {
        val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        try {
            val date = LocalDate.parse(billingCycle, inputFormat)
            val start = date.minusMonths(1).plusDays(1)
            val formattedBillingCycle =
                "${start.format(outputFormat)} - ${date.format(outputFormat)}"
            Timber.tag("VIJ").d("Formatted Billing Cycle $formattedBillingCycle")
            return formattedBillingCycle
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun formatDate(date: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyy")
        val formattedDate = LocalDate.parse(date, formatter)
        return formattedDate.format(outputFormatter)
    }

    fun getLastDue(dueDate: String?): String? {
        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyy")
            val formattedDate = LocalDate.parse(dueDate, formatter)
            val lastDue = formattedDate.minusMonths(1)
            return lastDue.format(outputFormatter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getFormattedAccountNumberForDisplay(accountNumber: String?): String {
        return accountNumber
            ?.chunked(4)
            ?.mapIndexed { index, chunk ->
                if (index < 3) "••••" else chunk
            }
            ?.joinToString(" ")
            .or("-")
    }

    @DrawableRes
    fun getLogo(cardType: String?): Int {
        return when (cardType) {
            "MASTERCARD" -> R.drawable.mastercard_logo
            else -> R.drawable.visa_logo
        }
    }

    fun getFormattedLimitForDisplay(creditCardLimit: String?): String {
        return "₹ ${formatToIndianNumberingSystem(creditCardLimit?.toLong() ?: 0L)}"
    }

    fun getFormattedCreditCardOutStandingForDisplay(creditCardOutStanding: Float?): String {
        return creditCardOutStanding.toString().or("-")
    }

    fun getProgress(creditCardLimit: String?, creditCardOutStanding: Float?): Float {
        return ((creditCardLimit?.toFloat() ?: 0f) - (creditCardOutStanding
            ?: 0f)) / (creditCardLimit?.toFloat() ?: 0f)
    }
}