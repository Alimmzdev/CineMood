package com.alimmzdev.cinemood_kmp.service.data.iranianmoviesapi.datasource

import com.alimmzdev.cinemood_kmp.service.data.iranianmoviesapi.dto.MovieDto
import com.alimmzdev.cinemood_kmp.service.data.iranianmoviesapi.dto.MoviesResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/**
 * Implementation of [MoviesRemoteDataSource] using Ktor HTTP client.
 * All network operations are executed on IO dispatcher.
 *
 * @property httpClient The Ktor HTTP client for making network requests
 * @property baseUrl The base URL of the movies API
 */
class MoviesRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String = "https://moviesapi.ir"
) : MoviesRemoteDataSource {

    override suspend fun fetchMovies(page: Int): Result<MoviesResponseDto> = withContext(Dispatchers.IO) {
        runCatching {
            val response: MoviesResponseDto = httpClient.get("$baseUrl/api/v1/movies") {
                parameter("page", page)
            }.body()
            response
        }
    }

    override suspend fun searchMovies(query: String, page: Int): Result<MoviesResponseDto> = withContext(Dispatchers.IO) {
        runCatching {
            val response: MoviesResponseDto = httpClient.get("$baseUrl/api/v1/movies") {
                parameter("q", query)
                parameter("page", page)
            }.body()
            response
        }
    }
}