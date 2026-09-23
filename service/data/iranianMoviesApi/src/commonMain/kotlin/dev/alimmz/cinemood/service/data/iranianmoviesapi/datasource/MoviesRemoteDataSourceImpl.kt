package dev.alimmz.cinemood.service.data.iranianmoviesapi.datasource

import dev.alimmz.cinemood.service.data.iranianmoviesapi.dto.MovieDetailDto
import dev.alimmz.cinemood.service.data.iranianmoviesapi.dto.MoviesResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of [dev.alimmz.cinemood.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSource] using Ktor HTTP client.
 * All network operations are executed on Default dispatcher for multiplatform compatibility.
 *
 * @property httpClient The Ktor HTTP client for making network requests
 * @property baseUrl The base URL of the movies API
 */
class MoviesRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String = "https://moviesapi.ir"
) : dev.alimmz.cinemood.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSource {

    override suspend fun fetchMovies(page: Int): Result<dev.alimmz.cinemood.service.data.iranianmoviesapi.dto.MoviesResponseDto> = withContext(Dispatchers.Default) {
        runCatching {
            val response: dev.alimmz.cinemood.service.data.iranianmoviesapi.dto.MoviesResponseDto = httpClient.get("$baseUrl/api/v1/movies") {
                parameter("page", page)
            }.body()
            response
        }
    }

    override suspend fun searchMovies(query: String, page: Int): Result<dev.alimmz.cinemood.service.data.iranianmoviesapi.dto.MoviesResponseDto> = withContext(Dispatchers.Default) {
        runCatching {
            val response: dev.alimmz.cinemood.service.data.iranianmoviesapi.dto.MoviesResponseDto = httpClient.get("$baseUrl/api/v1/movies") {
                parameter("q", query)
                parameter("page", page)
            }.body()
            response
        }
    }

    override suspend fun fetchMovieDetail(movieId: Int): Result<MovieDetailDto> = withContext(Dispatchers.Default) {
        runCatching {
            val response: MovieDetailDto = httpClient.get("$baseUrl/api/v1/movies/$movieId").body()
            response
        }
    }
}