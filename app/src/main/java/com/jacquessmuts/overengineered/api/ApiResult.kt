package com.jacquessmuts.overengineered.api

/**
 * A generic class that holds a value with its loading status.
 * Part of the Railway Oriented Programming Pattern, and Nomanini's analogue of the [kotlin.Result]
 * class.
 *
 * If you want to get a status without caring about the successful result, use **ApiResult Unit**
 */
sealed class ApiResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : ApiResult<T>()
    data class Error(val exception: Throwable) : ApiResult<Nothing>()

    // TODO: if contracts gain more support for handling `this` instance, these should get contracts
    val isSuccess: Boolean get() = this is Success
    val isFailure: Boolean get() = this is Error

    /**
     * Returns the encapsulated value if this instance represents [success][Result.isSuccess] or `null`
     * if it is [failure][Result.isFailure].
     */
    fun getOrNull(): T? =
        when (this) {
            is Error -> null
            is Success -> data
        }

    /**
     * Returns the encapsulated exception if this instance represents [Error] or `null`
     * if it is [Success].
     *
     * This function is shorthand for `fold(onSuccess = { null }, onFailure = { it })` (see [fold]).
     */
    fun exceptionOrNull(): Throwable? =
        when (this) {
            is Error -> exception
            else -> null
        }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }

    companion object {
        /**
         * This maps a given ApiResult into the newly required ApiResult<T>. You just have to pass it the (nullable)
         * success value. Use this if you want to convert one ApiResult of unknown status into another type.
         *
         * Please note that if you pass Error result, this will return a Failure regardless of successValue
         *
         * @param successValue the value of the successful result, if it exists. Must be non-null if it's successful.
         */
        fun <T : Any> fromResult(originalResult: ApiResult<*>, successValue: T?): ApiResult<T> {
            return if (originalResult is Success && successValue != null) {
                Success(successValue)
            } else if (originalResult is Error) {
                Error(originalResult.exception)
            } else {
                Error(IllegalArgumentException("Cannot convert $originalResult without successvalue, $successValue"))
            }
        }
    }
}
