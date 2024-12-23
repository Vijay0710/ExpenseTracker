package com.eyeshield.expensetracker.auth.data

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenRequest(
    val refresh_token: String? = null
)
