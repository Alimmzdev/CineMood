package com.alimmzdev.cinemood_kmp.core.domain.common

/**
 * Sealed class representing the result of an operation that can either succeed or fail.
 * This is an alternative to Kotlin's Result class with more detailed error handling.
 *
 * @param T The type of the success value
 */
sealed class BaseResult<out T> {

    /**
     * Represents a successful operation with the resulting data.
     *
     * @param data The successful result data
     */
    data class Success<out T>(val data: T) : BaseResult<T>()

    /**
     * Represents a failed operation with exception details.
     *
     * @param exception The exception that caused the failure
     * @param message Optional custom error message
     */
    data class Error(val exception: Exception, val message: String? = null) : BaseResult<Nothing>()

    /**
     * Checks if the result is successful.
     *
     * @return true if the result is Success, false otherwise
     */
    fun isSuccess(): Boolean = this is Success

    /**
     * Checks if the result is an error.
     *
     * @return true if the result is Error, false otherwise
     */
    fun isError(): Boolean = this is Error

    /**
     * Returns the data if successful, or null otherwise.
     *
     * @return The data if Success, null if Error
     */
    fun getOrNull(): T? = (this as? Success)?.data

    /**
     * Returns the data if successful, or throws the exception if Error.
     *
     * @return The data if Success
     * @throws Exception The exception from Error
     */
    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw exception
    }

    /**
     * Performs the given action on the data if the result is successful.
     *
     * @param action The action to perform on the success data
     * @return The same BaseResult instance for chaining
     */
    inline fun onSuccess(action: (T) -> Unit): BaseResult<T> {
        if (this is Success) action(data)
        return this
    }

    /**
     * Performs the given action on the error if the result is an error.
     *
     * @param action The action to perform on the error details
     * @return The same BaseResult instance for chaining
     */
    inline fun onError(action: (Exception, String?) -> Unit): BaseResult<T> {
        if (this is Error) action(exception, message)
        return this
    }
}

/**
 * Converts a standard Kotlin Result to BaseResult.
 *
 * @return BaseResult representation of the Result
 */
fun <T> Result<T>.toBaseResult(): BaseResult<T> {
    return fold(
        onSuccess = { BaseResult.Success(it) },
        onFailure = { BaseResult.Error(it as Exception) }
    )
}

/**
 * Converts a standard Kotlin [Result] to a [BaseResult] while applying a transformation
 * function to the success value.
 *
 * This overload is useful when you need to convert or map the data from one type to another
 * (e.g., converting a DTO to a domain model) while preserving the error state from the
 * original [Result].
 *
 * @param T The input type of the success value from the [Result]
 * @param R The output type after applying the transformation
 * @param transform A suspending function that transforms a value of type T to type R.
 *                  This function is only called if the [Result] is successful.
 * @return [BaseResult.Success] containing the transformed value if the [Result] is successful,
 *         or [BaseResult.Error] containing the exception if the [Result] is a failure.
 *
 * Example:
 * ```
 * // DTO to Domain Model conversion
 * data class UserDto(val id: Int, val name: String)
 * data class User(val id: Int, val name: String)
 *
 * val result: Result<UserDto> = Result.success(UserDto(1, "John"))
 * val baseResult = result.toBaseResult { dto ->
 *     User(id = dto.id, name = dto.name)
 * }
 * // baseResult is BaseResult.Success with User(1, "John")
 *
 * // Type transformation example
 * val stringResult: Result<String> = Result.success("42")
 * val intResult = stringResult.toBaseResult { str ->
 *     str.toIntOrNull() ?: throw IllegalArgumentException("Invalid number")
 * }
 * // intResult is BaseResult.Success with 42
 *
 * // With failure
 * val failedResult: Result<Double> = Result.failure(TimeoutException())
 * val transformed = failedResult.toBaseResult { value ->
 *     value.toInt()
 * }
 * // transformed is BaseResult.Error with the TimeoutException
 * ```
 *
 * @see toBaseResult for the version without transformation
 */
fun <T, R> Result<T>.toBaseResult(transform: (T) -> R): BaseResult<R> {
    return fold(
        onSuccess = { BaseResult.Success(transform(it)) },
        onFailure = { BaseResult.Error(it as Exception) }
    )
}

/**
 * Converts a BaseResult to a standard Kotlin Result.
 *
 * @return Kotlin Result representation of the BaseResult
 */
fun <T> BaseResult<T>.toKotlinResult(): Result<T> {
    return when (this) {
        is BaseResult.Success -> Result.success(data)
        is BaseResult.Error -> Result.failure(exception)
    }
}


/**
 * Extension function for BaseResult to map the success value to another type.
 *
 * @param transform The transformation function
 * @return New BaseResult with transformed data or the same error
 */
inline fun <T, R> BaseResult<T>.map(transform: (T) -> R): BaseResult<R> {
    return when (this) {
        is BaseResult.Success -> BaseResult.Success(transform(data))
        is BaseResult.Error -> BaseResult.Error(exception, message)
    }
}

/**
 * Extension function to combine a list of BaseResults into a single BaseResult.
 *
 * @return BaseResult containing list of all success values, or first error encountered
 */
fun <T> List<BaseResult<T>>.sequence(): BaseResult<List<T>> {
    val results = mutableListOf<T>()
    forEach { result ->
        when (result) {
            is BaseResult.Success -> results.add(result.data)
            is BaseResult.Error -> return BaseResult.Error(result.exception, result.message)
        }
    }
    return BaseResult.Success(results)
}

/**
 * Returns the success value or a default value if the result is an error.
 *
 * @param defaultValue The default value to return if result is Error
 * @return The success data or the default value
 */
fun <T> BaseResult<T>.getOrDefault(defaultValue: T): T {
    return (this as? BaseResult.Success)?.data ?: defaultValue
}

/**
 * Checks if the result is successful and the data satisfies the given predicate.
 *
 * @param predicate The condition to check on the success data
 * @return true if result is Success and predicate returns true, false otherwise
 */
inline fun <T> BaseResult<T>.isSuccessAnd(predicate: (T) -> Boolean): Boolean {
    return (this as? BaseResult.Success)?.let { predicate(it.data) } ?: false
}