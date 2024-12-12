package com.eyeshield.expensetracker.networking

import com.eyeshield.expensetracker.BuildConfig
import com.eyeshield.expensetracker.api.ApiResult
import com.eyeshield.expensetracker.api.DataError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Parameters
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <reified Response : Any> HttpClient.get(
    route: String,
    queryParameters: Map<String, Any?> = mapOf()
): ApiResult<Response, DataError.Network> {
    return safeCall {
        get {
            url(constructRoute(route))
            queryParameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }
}

suspend inline fun <reified Request : Any, reified Response : Any> HttpClient.post(
    route: String,
    body: Request? = null,
    vararg formPart: FormPart<*>
): ApiResult<Response, DataError.Network> {
    return safeCall {
        post {
            url(constructRoute(route))
            body?.let {
                setBody(body)
            }

            if (formPart.isNotEmpty()) {
                setBody(
                    FormDataContent(
                        Parameters.build {
                            formPart.map {
                                append(it.key, it.value.toString())
                            }
                        }
                    )
                )
            }
        }
    }
}

suspend inline fun <reified Request, reified Response : Any> HttpClient.patch(
    route: String,
    body: Request
): ApiResult<Response, DataError.Network> {
    return safeCall {
        patch {
            url(constructRoute(route))
            setBody(body)
        }
    }
}

suspend inline fun <reified Response : Any> HttpClient.delete(
    route: String,
    queryParameters: Map<String, Any?> = mapOf()
): ApiResult<Response, DataError.Network> {
    return safeCall {
        delete {
            url(constructRoute(route))
            queryParameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }
}

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): ApiResult<T, DataError.Network> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        e.printStackTrace()
        return ApiResult.ApiError(DataError.Network.NO_INTERNET)
    } catch (e: SerializationException) {
        return ApiResult.ApiError(DataError.Network.SERIALIZATION)
    } catch (e: Exception) {
        e.printStackTrace()
        if (e is CancellationException) throw e
        return ApiResult.ApiError(DataError.Network.UNKNOWN)
    }
    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(response: HttpResponse): ApiResult<T, DataError.Network> {
    return when (response.status.value) {
        in 200..299 -> ApiResult.Success(response.body<T>())
        401 -> ApiResult.ApiError(DataError.Network.UNAUTHORIZED)
        408 -> ApiResult.ApiError(DataError.Network.REQUEST_TIMED_OUT)
        409 -> ApiResult.ApiError(DataError.Network.CONFLICT)
        413 -> ApiResult.ApiError(DataError.Network.PAYLOAD_TOO_LARGE)
        429 -> ApiResult.ApiError(DataError.Network.TOO_MANY_REQUEST)
        in 500..599 -> ApiResult.ApiError(DataError.Network.SERVER_ERROR)
        else -> ApiResult.ApiError(DataError.Network.UNKNOWN)
    }
}

fun constructRoute(route: String): String {
    return when {
        route.contains(BuildConfig.BASE_URL) -> route
        route.startsWith("/") -> BuildConfig.BASE_URL + route
        else -> BuildConfig.BASE_URL + "/$route"
    }
}