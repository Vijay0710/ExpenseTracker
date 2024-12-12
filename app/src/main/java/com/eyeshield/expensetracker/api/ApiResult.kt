package com.eyeshield.expensetracker.api

sealed interface ApiResult<out D, out E> {

    data class Success<out D>(val data: D) : ApiResult<D, Nothing>

    data class ApiError<out E : Error>(val error: E) : ApiResult<Nothing, E>
}

inline fun <T, E : Error, R> ApiResult<T, E>.map(map: (T) -> R): ApiResult<R, E> {
    return when (this) {
        is ApiResult.ApiError -> ApiResult.ApiError(error)
        is ApiResult.Success -> ApiResult.Success(map(data))
    }
}

fun <T, E : Error> ApiResult<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map {}
}

typealias EmptyResult<E> = ApiResult<Unit, E>

// Empty placeholder for Errors
interface Error

sealed interface DataError : Error {

    enum class Network : DataError {
        REQUEST_TIMED_OUT,
        UNAUTHORIZED,
        CONFLICT,
        TOO_MANY_REQUEST,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }

    enum class Local : DataError {
        DISK_FULL
    }
}