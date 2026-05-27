package tech.nullexdev.cinemood.service.data.iranianmoviesapi.repository

import tech.nullexdev.cinemood.core.domain.common.BaseResult
import tech.nullexdev.cinemood.core.domain.common.toBaseResult
import tech.nullexdev.cinemood.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSource
import tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto.MovieDto
import tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto.MoviesResponseDto
import tech.nullexdev.cinemood.service.domain.moodel.Movie
import tech.nullexdev.cinemood.service.domain.moodel.MoviesPage
import tech.nullexdev.cinemood.service.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

/**
 * Implementation of [tech.nullexdev.cinemood.service.domain.repository.MoviesRepository] that fetches movie data from a remote API.
 *
 * This repository acts as a bridge between the data layer and domain layer by:
 * - Fetching raw data from [tech.nullexdev.cinemood.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSource] which returns standard Kotlin [Result]
 * - Converting [Result] types to [BaseResult] for consistent error handling in the domain layer
 * - Transforming DTOs (Data Transfer Objects) to domain models using [toDomainModel] extensions
 * - Providing reactive streams via [Flow] for real-time data emission
 *
 * The conversion flow follows this pattern:
 * ```
 * RemoteDataSource (returns Result<DTO>)
 *      ↓
 * Repository (converts using toBaseResult)
 *      ↓
 * Domain Layer (receives BaseResult<DomainModel>)
 * ```
 *
 * @property remoteDataSource The remote data source responsible for making HTTP API calls
 *
 * @see tech.nullexdev.cinemood.service.domain.repository.MoviesRepository
 * @see tech.nullexdev.cinemood.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSource
 * @see BaseResult
 * @see toBaseResult
 *
 * @author Alimmmzdev
 * @since 1.0.0
 */
class MoviesRepositoryImpl(
    private val remoteDataSource: tech.nullexdev.cinemood.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSource
) : tech.nullexdev.cinemood.service.domain.repository.MoviesRepository {

    /**
     * Retrieves a paginated list of movies as a reactive [Flow].
     *
     * This function:
     * 1. Calls [tech.nullexdev.cinemood.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSource.fetchMovies] to get a [Result] containing [tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto.MoviesResponseDto]
     * 2. Converts the [Result] to [BaseResult] using [toBaseResult] extension
     * 3. Transforms the DTO to domain model [tech.nullexdev.cinemood.service.domain.moodel.MoviesPage] via [toDomainModel] extension
     * 4. Emits the result as a [Flow] for reactive consumption
     *
     * @param page The page number to retrieve (1-indexed). Each page contains a predefined number of movies.
     * @return [Flow] emitting [BaseResult] with [tech.nullexdev.cinemood.service.domain.moodel.MoviesPage] on success or [BaseResult.Error] on failure.
     *         The flow emits a single value and completes.
     *
     * Example usage:
     * ```
     * repository.getMovies(1).collect { result ->
     *     when (result) {
     *         is BaseResult.Success -> {
     *             val moviesPage = result.data
     *             println("Loaded ${moviesPage.movies.size} movies from page ${moviesPage.currentPage}")
     *         }
     *         is BaseResult.Error -> {
     *             println("Error: ${result.message}")
     *         }
     *     }
     * }
     * ```
     *
     * @throws io.ktor.utils.io.CancellationException If the coroutine is canceled during execution
     * @see tech.nullexdev.cinemood.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSource.fetchMovies
     * @see BaseResult
     * @see tech.nullexdev.cinemood.service.domain.moodel.MoviesPage
     */
    override fun getMovies(page: Int): Flow<BaseResult<tech.nullexdev.cinemood.service.domain.moodel.MoviesPage>> = flow {
        val result = remoteDataSource.fetchMovies(page)
        val baseResult = result.toBaseResult { responseDto ->
            responseDto.toDomainModel()
        }
        emit(baseResult)
    }

    /**
     * Searches for movies by query string with pagination support as a reactive [Flow].
     *
     * This function provides a reactive stream of search results with automatic error handling
     * and transformation from DTO to domain model.
     *
     * @param query The search query string (movie name). Minimum length is 1 character.
     * @param page The page number to retrieve (1-indexed). Defaults to 1.
     * @return [Flow] emitting [BaseResult] with [tech.nullexdev.cinemood.service.domain.moodel.MoviesPage] or error
     *
     * Error scenarios handled:
     * - Empty or blank query → [BaseResult.Error] with message "Search query cannot be blank"
     * - Invalid page number → [BaseResult.Error] with message "Page number must be positive"
     * - Network failure → [BaseResult.Error] with network exception
     * - No results found → [BaseResult.Success] with empty movies list and totalCount = 0
     * - API error → [BaseResult.Error] with API exception message
     *
     * Example usage:
     * ```
     * viewModelScope.launch {
     *     repository.searchMovies("Inception", page = 1)
     *         .catch { exception ->
     *             _state.value = MoviesState.Error("Network error: ${exception.message}")
     *         }
     *         .collect { result ->
     *             when (result) {
     *                 is BaseResult.Success -> {
     *                     _state.value = MoviesState.Success(result.data)
     *                 }
     *                 is BaseResult.Error -> {
     *                     _state.value = MoviesState.Error(result.message ?: "Search failed")
     *                 }
     *             }
     *         }
     * }
     * ```
     *
     * @throws IllegalArgumentException if query is blank or page is invalid
     * @throws io.ktor.utils.io.CancellationException if the coroutine is cancelled
     * @see [getMovies] for unfiltered movie listing
     * @see [BaseResult] for result handling patterns
     */
    override fun searchMovies(query: String, page: Int): Flow<BaseResult<tech.nullexdev.cinemood.service.domain.moodel.MoviesPage>> = flow {
        // Input validation
        require(query.isNotBlank()) { "Search query cannot be blank" }
        require(page > 0) { "Page number must be positive, got $page" }

        println("Searching movies with query: '$query', page: $page")

        val result = remoteDataSource.searchMovies(query, page)
        val baseResult = result.toBaseResult { responseDto ->
            responseDto.toDomainModel()
        }

        // Optional: Log search results
        if (baseResult is BaseResult.Success) {
            println("Search completed: found ${baseResult.data} movies")
        } else if (baseResult is BaseResult.Error) {
            println("Search failed: ${baseResult.message}")
        }

        emit(baseResult)
    }.catch { exception ->
        // Catch any unexpected errors
        emit(BaseResult.Error(
            exception = exception as? Exception ?: Exception(exception),
            message = "Unexpected error during search: ${exception.message}"
        ))
    }
}