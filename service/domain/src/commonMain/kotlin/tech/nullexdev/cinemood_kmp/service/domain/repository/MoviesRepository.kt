package tech.nullexdev.cinemood.service.domain.repository

import tech.nullexdev.cinemood.core.domain.common.BaseResult
import tech.nullexdev.cinemood.core.domain.repository.BaseRepository
import tech.nullexdev.cinemood.service.domain.moodel.Movie
import tech.nullexdev.cinemood.service.domain.moodel.MoviesPage
import kotlinx.coroutines.flow.Flow


/**
 * Repository interface for movie-related data operations.
 * Follows the project's architecture with BaseResult and reactive streams.
 */
interface MoviesRepository : BaseRepository {

    /**
     * Retrieves a paginated list of movies as a reactive Flow.
     *
     * @param page The page number to retrieve (1-indexed)
     * @return Flow emitting BaseResult with MoviesPage or Error
     */
    fun getMovies(page: Int): Flow<BaseResult<tech.nullexdev.cinemood.service.domain.moodel.MoviesPage>>

    /**
     * Searches for movies by query string with pagination support.
     * This replaces the old getMovieById method.
     *
     * @param query The search query (movie name) - required
     * @param page The page number to retrieve (1-indexed, optional, defaults to 1)
     * @return Flow emitting BaseResult with MoviesPage containing search results or Error
     *
     * Example:
     * ```
     * repository.searchMovies("Shawshank", page = 1).collect { result ->
     *     when (result) {
     *         is BaseResult.Success -> {
     *             val movies = result.data.movies
     *             println("Found ${movies.size} movies")
     *         }
     *         is BaseResult.Error -> {
     *             println("Search failed: ${result.message}")
     *         }
     *     }
     * }
     * ```
     */
    fun searchMovies(query: String, page: Int = 1): Flow<BaseResult<tech.nullexdev.cinemood.service.domain.moodel.MoviesPage>>
}