// domain/repository/BaseRepository.kt
package com.alimmzdev.cinemood_kmp.core.domain.repository

import com.alimmzdev.cinemood_kmp.core.domain.common.BaseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Converts a suspend function into a Flow that emits BaseResult.
 * This function automatically handles coroutine contexts and error catching.
 *
 * @param operationName Name of the operation for logging purposes
 * @param dispatcher The coroutine dispatcher to use (defaults to Dispatchers.IO)
 * @param block The suspend function to execute
 * @return Flow emitting BaseResult containing either success data or error
 *
 * Usage example:
 * ```
 * fun fetchMovies(page: Int): Flow<BaseResult<MoviesPage>> = executeToFlow(
 *     operationName = "fetchMovies(page=$page)",
 *     dispatcher = Dispatchers.IO
 * ) {
 *     api.getMovies(page)
 * }
 * ```
 */
fun <T> executeToFlow(
    operationName: String,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> T
): Flow<BaseResult<T>> = flow {
    println("Starting: $operationName on $dispatcher")
    try {
        val result = block()
        println("Finished: $operationName - Success")
        emit(BaseResult.Success(result))
    } catch (e: Exception) {
        println("Finished: $operationName - Error: ${e.message}")
        emit(BaseResult.Error(e))
    }
}.flowOn(dispatcher)
    .catch { e ->
        emit(BaseResult.Error(e as? Exception ?: Exception(e)))
    }

/**
 * Convenience function for IO operations (network, database, file system).
 * Uses Dispatchers.IO by default.
 *
 * @param operationName Name of the operation for logging
 * @param block The suspend function to execute
 * @return Flow emitting BaseResult
 */
fun <T> executeToIoFlow(
    operationName: String,
    block: suspend () -> T
): Flow<BaseResult<T>> = executeToFlow(operationName, Dispatchers.IO, block)

/**
 * Convenience function for CPU-intensive operations (parsing, transformations).
 * Uses Dispatchers.Default by default.
 *
 * @param operationName Name of the operation for logging
 * @param block The suspend function to execute
 * @return Flow emitting BaseResult
 */
fun <T> executeToDefaultFlow(
    operationName: String,
    block: suspend () -> T
): Flow<BaseResult<T>> = executeToFlow(operationName, Dispatchers.Default, block)

/**
 * Marker interface for all repository classes.
 * This interface is used to identify repository classes for extension functions.
 */
interface BaseRepository

/**
 * Extension function for any BaseRepository to execute operations with automatic logging.
 * This utility function does not force implementation on repositories but provides
 * convenient logging capabilities.
 *
 * @param operationName Name of the operation being executed
 * @param block The operation to execute (as a suspend function on the repository)
 * @return BaseResult containing the operation result
 *
 * Usage example:
 * ```
 * override suspend fun getMovies(page: Int): BaseResult<MoviesPage> {
 *     return executeWithLogging("getMovies(page=$page)") {
 *         val response = remoteDataSource.fetchMovies(page)
 *         BaseResult.Success(response.toDomainModel())
 *     }
 * }
 * ```
 */
suspend fun <T : BaseRepository> T.executeWithLogging(
    operationName: String,
    block: suspend T.() -> BaseResult<*>
): BaseResult<*> {
    println("Starting: $operationName on ${this::class.simpleName}")
    val result = block()
    val status = if (result.isSuccess()) "Success" else "Error"
    println("Finished: $operationName with result: $status")
    return result
}