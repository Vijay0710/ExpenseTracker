package com.eyeshield.expensetracker.auth

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenRequest(
    val refresh_token: String? = null
)
