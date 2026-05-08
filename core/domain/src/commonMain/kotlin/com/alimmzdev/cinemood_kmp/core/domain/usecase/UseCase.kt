package com.alimmzdev.cinemood_kmp.core.domain.usecase

/**
 * Base class for all use cases (command style).
 * Use case with input parameters and output type.
 */
abstract class UseCase<in P, R> {
    abstract suspend fun execute(params: P): Result<R>

    suspend operator fun invoke(params: P): Result<R> = execute(params)
}

/**
 * Use case without input parameters.
 */
abstract class NoArgUseCase<R> {
    abstract suspend fun execute(): Result<R>

    suspend operator fun invoke(): Result<R> = execute()
}

/**
 * Simple use case that returns raw data (no Result wrapper).
 * Useful when caller handles errors manually.
 */
abstract class SimpleUseCase<in P, R> {
    abstract suspend fun execute(params: P): R

    suspend operator fun invoke(params: P): R = execute(params)
}