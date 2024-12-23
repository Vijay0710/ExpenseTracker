package com.eyeshield.expensetracker.data.mapper

import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.data.local.entity.CreditAccount
import com.eyeshield.expensetracker.data.remote.CreditAccountDTO
import com.eyeshield.expensetracker.extensions.or
import com.eyeshield.expensetracker.home_graph.home.data.CreditAccountUIModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun CreditAccountDTO.toEntity(): CreditAccount {
    return CreditAccount(
        id = id!!,
        bankName = bankName,
        creditCardOutStanding = creditCardOutStanding,
        creditCardLimit = creditCardLimit,
        cardType = cardType,
        creditCardDueDate = creditCardDueDate,
        accountNumber = accountNumber,
    )
}

fun CreditAccountDTO.toUIModel(): CreditAccountUIModel {
    return CreditAccountUIModel(
        id = id!!,
        accountNumber = this.accountNumber
            ?.chunked(4)
            ?.mapIndexed { index, chunk ->
                if (index < 3) "••••" else chunk
            }
            ?.joinToString(" ")
            .or("-"),
        logo = when (this.cardType) {
            "MASTERCARD" -> R.drawable.mastercard_logo
            else -> R.drawable.visa_logo
        },
        cardLimit = "₹ ${formatToIndianNumberingSystem(creditCardLimit?.toLong() ?: 0L)}",
        progress = ((creditCardLimit?.toFloat() ?: 0f) - (creditCardOutStanding
            ?: 0f)) / (creditCardLimit?.toFloat() ?: 0f),
        creditCardOutStanding = creditCardOutStanding.toString().or("-")
    )
}

fun CreditAccount.toUIModel(): CreditAccountUIModel {
    return CreditAccountUIModel(
        id = id,
        accountNumber = this.accountNumber
            ?.chunked(4)
            ?.mapIndexed { index, chunk ->
                if (index < 3) "••••" else chunk
            }
            ?.joinToString(" ")
            .or("-"),
        logo = when (this.cardType) {
            "MASTERCARD" -> R.drawable.mastercard_logo
            else -> R.drawable.visa_logo
        },
        cardLimit = "₹ ${formatToIndianNumberingSystem(creditCardLimit?.toLong() ?: 0L)}",
        progress = ((creditCardLimit?.toFloat() ?: 0f) - (creditCardOutStanding
            ?: 0f)) / (creditCardLimit?.toFloat() ?: 0f),
        creditCardOutStanding = creditCardOutStanding.toString().or("-"),
    )
}

fun List<CreditAccountDTO>.toUIModelListFromDTO(): List<CreditAccountUIModel> {
    return map { it.toUIModel() }
}

fun List<CreditAccountDTO>.toEntityModelList(): List<CreditAccount> {
    return map { it.toEntity() }
}

fun List<CreditAccount>.toUIModelListFromEntity(): List<CreditAccountUIModel> {
    return map { it.toUIModel() }
}

private fun formatToIndianNumberingSystem(number: Long): String {
    val symbols = DecimalFormatSymbols(Locale("en", "IN"))
    val formatter = DecimalFormat("#,##,###.##", symbols)
    return formatter.format(number)
}