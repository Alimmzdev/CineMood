package tech.nullexdev.cinemood.core.domain.usecase

import tech.nullexdev.cinemood.core.domain.common.BaseResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow

/**
 * Base interface for all use cases in the domain layer.
 * Use cases encapsulate business logic and can be easily tested.
 *
 * @param P The parameter type for the use case (input)
 * @param R The result type that will be emitted (output)
 */
interface BaseUseCase<in P, R> {

    /**
     * Executes the use case with the given parameters.
     *
     * @param parameters The input parameters for the use case
     * @return Flow emitting BaseResult with the result or error
     */
    operator fun invoke(parameters: P): Flow<tech.nullexdev.cinemood.core.domain.common.BaseResult<R>>
}

/**
 * Base interface for use cases without parameters.
 *
 * @param R The result type that will be emitted
 */
interface BaseUseCaseNoParams<R> {

    /**
     * Executes the use case without any parameters.
     *
     * @return Flow emitting BaseResult with the result or error
     */
    operator fun invoke(): Flow<tech.nullexdev.cinemood.core.domain.common.BaseResult<R>>
}

/**
 * Extension function to execute a use case with automatic logging.
 *
 * @param operationName Name of the operation for logging
 * @param block The suspend function to execute
 */
fun <R> executeUseCase(
    operationName: String,
    block: suspend () -> R
): Flow<tech.nullexdev.cinemood.core.domain.common.BaseResult<R>> = flow {
    println("Executing use case: $operationName")
    try {
        val result = block()
        println("Use case completed: $operationName - Success")
        emit(_root_ide_package_.tech.nullexdev.cinemood.core.domain.common.BaseResult.Success(result))
    } catch (e: Exception) {
        println("Use case failed: $operationName - Error: ${e.message}")
        emit(_root_ide_package_.tech.nullexdev.cinemood.core.domain.common.BaseResult.Error(e))
    }
}