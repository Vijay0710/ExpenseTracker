@file:OptIn(ExperimentalSerializationApi::class)

package com.eyeshield.expensetracker.data.remote

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class CreditAccountDTO(
    val id: String? = null,
    @JsonNames("bank_name")
    val bankName: String? = null,
    @JsonNames("credit_card_limit")
    val creditCardLimit: String? = null,
    @JsonNames("credit_card_due_date")
    val creditCardDueDate: String? = null,
    @JsonNames("billing_cycle")
    val billingCycle: String? = null,
    @JsonNames("credit_card_outstanding")
    val creditCardOutStanding: Float? = null,
    @JsonNames("account_number")
    val accountNumber: String? = null,
    @JsonNames("card_type")
    val cardType: String? = null,
    @JsonNames("total_reward_points")
    val totalRewardPoints: String? = null
)
