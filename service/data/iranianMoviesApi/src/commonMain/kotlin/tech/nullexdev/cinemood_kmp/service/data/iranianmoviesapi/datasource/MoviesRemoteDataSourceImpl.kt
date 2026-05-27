package tech.nullexdev.cinemood.service.data.iranianmoviesapi.datasource

import tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto.MovieDto
import tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto.MoviesResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/**
 * Implementation of [tech.nullexdev.cinemood.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSource] using Ktor HTTP client.
 * All network operations are executed on IO dispatcher.
 *
 * @property httpClient The Ktor HTTP client for making network requests
 * @property baseUrl The base URL of the movies API
 */
class MoviesRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String = "https://moviesapi.ir"
) : tech.nullexdev.cinemood.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSource {

    override suspend fun fetchMovies(page: Int): Result<tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto.MoviesResponseDto> = withContext(Dispatchers.IO) {
        runCatching {
            val response: tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto.MoviesResponseDto = httpClient.get("$baseUrl/api/v1/movies") {
                parameter("page", page)
            }.body()
            response
        }
    }

    override suspend fun searchMovies(query: String, page: Int): Result<tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto.MoviesResponseDto> = withContext(Dispatchers.IO) {
        runCatching {
            val response: tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto.MoviesResponseDto = httpClient.get("$baseUrl/api/v1/movies") {
                parameter("q", query)
                parameter("page", page)
            }.body()
            response
        }
    }
}