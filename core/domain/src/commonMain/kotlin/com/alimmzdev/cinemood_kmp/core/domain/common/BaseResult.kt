package com.alimmzdev.cinemood_kmp.core.domain.common

import com.alimmzdev.cinemood_kmp.core.domain.exception.BaseException

/** Represents a result that can be success or failure */
sealed class BaseResult<out T> {

    data class Success<T>(val data: T) : BaseResult<T>()

    data class Error(
        val exception: BaseException,
        val message: String? = exception.message
    ) : BaseResult<Nothing>()

    object Loading : BaseResult<Nothing>()

    // Convenience methods
    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error
    fun isLoading(): Boolean = this is Loading

    inline fun onSuccess(action: (T) -> Unit): BaseResult<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (Error) -> Unit): BaseResult<T> {
        if (this is Error) action(this)
        return this
    }

    inline fun onLoading(action: () -> Unit): BaseResult<T> {
        if (this is Loading) action()
        return this
    }

    fun getOrNull(): T? = if (this is Success) data else null

    fun exceptionOrNull(): BaseException? = if (this is Error) exception else null
}