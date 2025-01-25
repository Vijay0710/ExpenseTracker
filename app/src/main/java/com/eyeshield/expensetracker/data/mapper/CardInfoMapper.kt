package com.eyeshield.expensetracker.data.mapper

import com.eyeshield.expensetracker.cards.data.CardsInfoUIModel
import com.eyeshield.expensetracker.cards.data.CreditCardInfoUIModel
import com.eyeshield.expensetracker.data.local.entity.CreditAccount
import com.eyeshield.expensetracker.data.mapper.utils.CardUtils
import com.eyeshield.expensetracker.extensions.or
import com.eyeshield.expensetracker.home_graph.home.data.CreditAccountUIModel

fun CreditAccount.toCardInfoUIModel(): CardsInfoUIModel {
    return CardsInfoUIModel(
        totalLimit = "₹ ${CardUtils.formatToIndianNumberingSystem(creditCardLimit?.toLong() ?: 0L)}",
        billingCycle = CardUtils.formatBillingCycle(billingCycle.or("1970/01/01")),
        dueDate = CardUtils.formatDate(creditCardDueDate.or("1970/01/01")),
        lastDueDate = CardUtils.getLastDue(creditCardDueDate).or("1970/01/01"),
        totalRewardsPoints = totalRewardPoints.or("-")
    )
}

fun CreditAccount.toCreditCardInfoUIModel(): CreditCardInfoUIModel {
    return CreditCardInfoUIModel(
        id = id,
        accountNumber = CardUtils.getFormattedAccountNumberForDisplay(accountNumber),
        cardLimit = CardUtils.getFormattedLimitForDisplay(creditCardLimit),
        bankName = bankName.or("-"),
        creditCardOutStanding = CardUtils.getFormattedCreditCardOutStandingForDisplay(
            creditCardOutStanding
        ),
        logo = CardUtils.getLogo(cardType),
        progress = CardUtils.getProgress(creditCardLimit, creditCardOutStanding),
    )
}

fun List<CreditAccount>.toCardInfoUIModelListFromEntity(): List<CardsInfoUIModel> {
    return map { it.toCardInfoUIModel() }
}

fun List<CreditAccount>.toCreditAccountInfoUIModelListFromEntity(): List<CreditCardInfoUIModel> {
    return map { it.toCreditCardInfoUIModel() }
}

fun CreditCardInfoUIModel.toCreditAccountUIModel(): CreditAccountUIModel {
    return CreditAccountUIModel(
        id = id,
        accountNumber = accountNumber,
        cardLimit = cardLimit,
        creditCardOutStanding = creditCardOutStanding,
        logo = logo,
        progress = progress
    )
}