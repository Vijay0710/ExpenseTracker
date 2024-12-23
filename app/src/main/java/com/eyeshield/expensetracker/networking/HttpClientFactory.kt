package com.eyeshield.expensetracker.networking

import com.eyeshield.expensetracker.BuildConfig
import com.eyeshield.expensetracker.EncryptedSessionStorage
import com.eyeshield.expensetracker.api.ApiResult
import com.eyeshield.expensetracker.auth.data.AccessTokenRequest
import com.eyeshield.expensetracker.auth.data.AccessTokenResponse
import com.eyeshield.expensetracker.auth.data.AuthInfo
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HttpClientFactory @Inject constructor(
    private val encryptedSessionStorage: EncryptedSessionStorage
) {
    fun build(): HttpClient {
        return HttpClient(CIO) {
            install(HttpTimeout) {
                requestTimeoutMillis = 60_000
                connectTimeoutMillis = 60_000
                socketTimeoutMillis = 60_000
            }

            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.tag("VIJ").d(message)
                    }
                }
                level = LogLevel.ALL
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
                header("X-Auth-Basic", "Basic ${BuildConfig.API_VALIDATION_KEY}")
            }

            install(Auth) {
                bearer {
                    // Shouldn't add token to login call
                    sendWithoutRequest { request ->
                        val endpoint = request.url.encodedPath
                        !endpoint.contains(
                            "/auth/token",
                            ignoreCase = true
                        ) || !endpoint.contains("/auth/verify_token", ignoreCase = true)
                    }

                    loadTokens {
                        val info = encryptedSessionStorage.get()
                        BearerTokens(
                            accessToken = info?.accessToken.orEmpty(),
                            refreshToken = info?.refreshToken.orEmpty()
                        )
                    }

                    refreshTokens {

                        val info = encryptedSessionStorage.get()
                        val response = client.post<AccessTokenRequest, AccessTokenResponse>(
                            route = "/auth/refresh_token",
                            body = AccessTokenRequest(
                                refresh_token = info?.refreshToken.orEmpty()
                            )
                        )

                        if (response is ApiResult.Success) {
                            val newAuthInfo = AuthInfo(
                                accessToken = response.data.accessToken,
                                refreshToken = info?.refreshToken.orEmpty(),
                                userId = info?.userId.orEmpty()
                            )

                            encryptedSessionStorage.set(newAuthInfo)

                            BearerTokens(
                                accessToken = newAuthInfo.accessToken.orEmpty(),
                                refreshToken = info?.refreshToken.orEmpty()
                            )

                        } else {
                            BearerTokens(
                                accessToken = "",
                                refreshToken = ""
                            )
                        }
                    }
                }
            }
        }
    }
}