package com.eyeshield.expensetracker.database

enum class DatabaseStatus {
    SUCCESS,
    LOADING,
    ERROR
}

fun DatabaseStatus?.orLoading(): DatabaseStatus {
    return this ?: DatabaseStatus.LOADING
}

/**
 * @param status represents Database Status such as Success, Loading and Error
 * @param _data represents the data of the DatabaseResult
 * @param exception represents the exception raised in case of Any failure
 * @see
 * Declaring ApiResult as out means we can use the instantiated class to its subtype, also known as covariance
 */
sealed class DatabaseResult<out T>(
    val status: DatabaseStatus = DatabaseStatus.SUCCESS,
    val _data: T? = null,
    val exception: Exception? = null
) {
    data class Success<R>(val data: R) : DatabaseResult<R>(
        _data = data,
        status = DatabaseStatus.SUCCESS
    )

    class Loading : DatabaseResult<Nothing>(
        status = DatabaseStatus.LOADING
    )

    data class Error<R> (val error: Exception) : DatabaseResult<R>(
        status = DatabaseStatus.ERROR,
        exception = error
    )
}
