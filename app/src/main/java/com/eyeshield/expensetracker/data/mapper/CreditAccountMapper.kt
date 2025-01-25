package com.eyeshield.expensetracker.data.mapper

import com.eyeshield.expensetracker.data.local.entity.CreditAccount
import com.eyeshield.expensetracker.data.mapper.utils.CardUtils
import com.eyeshield.expensetracker.data.remote.CreditAccountDTO
import com.eyeshield.expensetracker.home_graph.home.data.CreditAccountUIModel

fun CreditAccountDTO.toEntity(): CreditAccount {
    return CreditAccount(
        id = id!!,
        bankName = bankName,
        creditCardOutStanding = creditCardOutStanding,
        creditCardLimit = creditCardLimit,
        cardType = cardType,
        creditCardDueDate = creditCardDueDate,
        accountNumber = accountNumber,
        totalRewardPoints = totalRewardPoints,
        billingCycle = billingCycle
    )
}

fun CreditAccountDTO.toUIModel(): CreditAccountUIModel {
    return CreditAccountUIModel(
        id = id!!,
        accountNumber = CardUtils.getFormattedAccountNumberForDisplay(accountNumber),
        logo = CardUtils.getLogo(cardType),
        cardLimit = CardUtils.getFormattedLimitForDisplay(creditCardLimit),
        progress = CardUtils.getProgress(creditCardLimit, creditCardOutStanding),
        creditCardOutStanding = CardUtils.getFormattedCreditCardOutStandingForDisplay(
            creditCardOutStanding
        )
    )
}

fun CreditAccount.toUIModel(): CreditAccountUIModel {
    return CreditAccountUIModel(
        id = id,
        accountNumber = CardUtils.getFormattedAccountNumberForDisplay(accountNumber),
        logo = CardUtils.getLogo(cardType),
        cardLimit = CardUtils.getFormattedLimitForDisplay(creditCardLimit),
        progress = CardUtils.getProgress(creditCardLimit, creditCardOutStanding),
        creditCardOutStanding = CardUtils.getFormattedCreditCardOutStandingForDisplay(
            creditCardOutStanding
        ),
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