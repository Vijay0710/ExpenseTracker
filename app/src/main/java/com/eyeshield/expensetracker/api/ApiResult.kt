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

    enum class Network(val message: String = "Something went wrong on our end! Our team is on it, armed with coffee and determination!") :
        DataError {
        REQUEST_TIMED_OUT(message = "Oops! Your request took too long to respond. Can you check your internet connection and try again later"),
        UNAUTHORIZED,
        CONFLICT,
        TOO_MANY_REQUEST,
        NO_INTERNET(message = "Looks like we’ve lost connection to the internet! Kindly ensure you're connected and give it another shot."),
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }

    /**
     * TODO: To handle local storage
     * **/
    enum class Local : DataError {
        DISK_FULL
    }
}