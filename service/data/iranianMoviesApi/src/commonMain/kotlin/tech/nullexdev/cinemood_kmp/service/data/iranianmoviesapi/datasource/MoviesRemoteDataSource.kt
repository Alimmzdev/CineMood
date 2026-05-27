package tech.nullexdev.cinemood.service.data.iranianmoviesapi.datasource

import tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto.MovieDto
import tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto.MoviesResponseDto

/**
 * Remote data source interface for movie API operations.
 * This layer returns standard Kotlin Result type.
 */
interface MoviesRemoteDataSource {

    /**
     * Fetches a paginated list of movies from the remote API.
     *
     * @param page The page number to fetch (1-indexed)
     * @return [Result] containing [tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto.MoviesResponseDto] or exception
     */
    suspend fun fetchMovies(page: Int): Result<tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto.MoviesResponseDto>

    /**
     * Searches for movies by query string from the remote API.
     *
     * This function performs a search request to the movies API endpoint with query parameters.
     * The search is performed on both:
     * - Movies from the web service database
     * - Movies registered by users of the web service
     *
     * @param query The search query string (movie name) - required parameter
     * @param page The page number to retrieve (1-indexed, optional, defaults to 1 if not specified)
     * @return [Result] containing [tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto.MoviesResponseDto] with search results or exception
     *
     * Example:
     * ```
     * // Search for movies with "Shawshank" in the title
     * val result = dataSource.searchMovies("Shawshank", page = 1)
     * result.onSuccess { response ->
     *     println("Found ${response.metadata.total_count} movies")
     *     response.data.forEach { movie ->
     *         println("- ${movie.title}")
     *     }
     * }.onFailure { error ->
     *     println("Search failed: ${error.message}")
     * }
     * ```
     *
     * @throws kotlinx.io.IOException If network error occurs
     * @throws kotlinx.serialization.SerializationException If response cannot be parsed
     * @see tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto.MoviesResponseDto
     * @see tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto.MovieDto
     */
    suspend fun searchMovies(query: String, page: Int = 1): Result<tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto.MoviesResponseDto>
}
