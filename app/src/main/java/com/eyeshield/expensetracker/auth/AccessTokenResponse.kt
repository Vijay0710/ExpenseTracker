@file:OptIn(ExperimentalSerializationApi::class)

package com.eyeshield.expensetracker.auth

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class AccessTokenResponse(
    @JsonNames("access_token")
    val accessToken: String? = null,
    @JsonNames("user_id")
    val userId: String? = null
)