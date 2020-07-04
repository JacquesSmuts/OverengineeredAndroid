package com.jacquessmuts.overengineered.api

import timber.log.Timber

/**
 * A generic class that holds a value with its loading status.
 * Part of the Railway Oriented Programming Pattern, and Nomanini's analogue of the [kotlin.Result]
 * class.
 *
 * If you want to get a status without caring about the successful result, use **ApiResult Unit**
 */
sealed class ApiResult<T> {

    val isSuccess: Boolean get() = this is Success
    val isFailure: Boolean get() = this is Failure

    /**
     * Returns the encapsulated value if this instance represents [success][Result.isSuccess] or `null`
     * if it is [failure][Result.isFailure].
     */
    fun getOrNull(): T? =
        when (this) {
            is Failure -> null
            is Success -> data
        }

    /**
     * Returns the encapsulated exception if this instance represents [Failure] or `null`
     * if it is [Success].
     *
     * This function is shorthand for `fold(onSuccess = { null }, onFailure = { it })` (see [fold]).
     */
    fun exceptionOrNull(): Throwable? =
        when (this) {
            is Failure -> exception
            else -> null
        }

    /**
     * Determines if this class is a failed result, then logs an error or warning if true
     */
    fun logErrors(): ApiResult<T> {
        if (this is Failure) {
            Timber.e(exception)
        }
        return this
    }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Failure -> "Failure[exception=$exception]"
        }
    }

    companion object {
        /**
         * This maps a given ApiResult into the newly required ApiResult<T>. You just have to pass it the (nullable)
         * success value. Use this if you want to convert one ApiResult of unknown status into another type.
         *
         * Please note that if you pass Failure result, this will return a Failure regardless of successValue
         *
         * @param successValue the value of the successful result, if it exists. Must be non-null if it's successful.
         */
        fun <T> fromResult(originalResult: ApiResult<*>, successValue: T?): ApiResult<T> {

            return if (originalResult is Success && successValue != null) {
                Success(successValue)
            } else if (originalResult is Failure) {
                Failure(originalResult.exception)
            } else {
                Failure(IllegalArgumentException("Cannot convert $originalResult without successvalue, $successValue"))
            }
        }
    }
}

data class Success<T>(val data: T) : ApiResult<T>()
data class Failure<T>(val exception: Throwable) : ApiResult<T>()

/**
 * Run the function, and pass the value on regardless
 */
suspend infix fun <T> ApiResult<T>.onSuccess(function: suspend (T) -> Unit): ApiResult<T> =
    when (this) {
        is Success -> {
            function(this.data)
            this
        }
        is Failure -> this
    }

/**
 * Run the function, and pass the value on regardless
 */
infix fun <T> ApiResult<T>.onSuccessBlocking(function: (T) -> Unit): ApiResult<T> =
    when (this) {
        is Success -> {
            function(this.data)
            this
        }
        is Failure -> this
    }

/**
 * Map the successful ApiResult Type into a different Type.
 */
suspend fun <In, Out> ApiResult<In>.mapSuccess(map: suspend (In) -> Out): ApiResult<Out> {

    return if (this is Success) {
        ApiResult.fromResult(this, map(this.data))
    } else {
        ApiResult.fromResult(this, null)
    }
}

/**
 * Map the Failed ApiResult Throwable into a different Throwable
 */
suspend fun <T> ApiResult<T>.mapFailure(map: suspend (Throwable) -> Throwable): ApiResult<T> {

    return if (this is Failure) {
        Failure(map(this.exception))
    } else {
        this
    }
}

/**
 * Pass on the success, or handle the failure
 */
suspend infix fun <T> ApiResult<T>.onFailure(function: suspend (Throwable) -> Unit): ApiResult<T> =
    when (this) {
        is Success -> this
        is Failure -> {
            function(this.exception)
            this
        }
    }

/**
 * Run the function, and pass the value on
 */
infix fun <T> ApiResult<T>.onFailureBlocking(function: (Throwable) -> Unit): ApiResult<T> =
    when (this) {
        is Success -> this
        is Failure -> {
            function(this.exception)
            this
        }
    }

/**
 * do something regardless of success or failure
 */
suspend infix fun <T> ApiResult<T>.also(function: suspend (T?) -> Unit): ApiResult<T> {
    function(this.getOrNull())
    return this
}

/**
 * do something regardless of success or failure
 */
infix fun <T> ApiResult<T>.alsoBlocking(function: (T?) -> Unit): ApiResult<T> {
    function(this.getOrNull())
    return this
}
