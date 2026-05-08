package com.alimmzdev.cinemood_kmp.core.domain.repository

import com.alimmzdev.cinemood_kmp.core.domain.common.BaseResult

/**
 * Base marker interface for all repositories.
 *
 * This interface doesn't define any common methods because in Clean Architecture,
 * each repository should have its own specific methods based on business requirements.
 *
 * Just for type constraint and generic programming.
 */
interface BaseRepository

/**
 * Extension function for any BaseRepository
 * Example of a utility that doesn't force implementation
 */
suspend fun <T : BaseRepository> T.executeWithLogging(
    operationName: String,
    block: suspend T.() -> BaseResult<*>
): BaseResult<*> {
    Result
    println("Starting: $operationName on ${this::class.simpleName}")
    val result = block()
    println("Finished: $operationName with result: ${if (result.isSuccess()) "Success" else "Error"}")
    return result
}