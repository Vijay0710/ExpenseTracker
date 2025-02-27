@file:OptIn(ExperimentalSerializationApi::class)

package com.eyeshield.expensetracker.auth.data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class AuthInfo(
    @JsonNames("access_token")
    val accessToken: String? = null,
    @JsonNames("refresh_token")
    val refreshToken: String? = null,
    @JsonNames("user_id")
    val userId: String? = null
)
