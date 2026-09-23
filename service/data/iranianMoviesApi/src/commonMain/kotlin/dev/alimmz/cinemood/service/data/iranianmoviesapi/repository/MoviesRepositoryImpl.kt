package dev.alimmz.cinemood.service.data.iranianmoviesapi.repository

import dev.alimmz.cinemood.core.domain.common.BaseResult
import dev.alimmz.cinemood.core.domain.common.toBaseResult
import dev.alimmz.cinemood.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSource
import dev.alimmz.cinemood.service.domain.model.MovieDetail
import dev.alimmz.cinemood.service.domain.model.MoviesPage
import dev.alimmz.cinemood.service.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

class MoviesRepositoryImpl(
    private val remoteDataSource: MoviesRemoteDataSource
) : MoviesRepository {

    override fun getMovies(page: Int): Flow<BaseResult<MoviesPage>> = flow {
        val result = remoteDataSource.fetchMovies(page)
        val baseResult = result.toBaseResult(
            errorMapper = { it.asUiMessage() }
        ) { responseDto ->
            responseDto.toDomainModel()
        }
        emit(baseResult)
    }

    override fun getMovieDetail(movieId: Int): Flow<BaseResult<MovieDetail>> = flow {
        require(movieId > 0) { "Movie id must be positive, got $movieId" }
        val result = remoteDataSource.fetchMovieDetail(movieId)
        val baseResult = result.toBaseResult(
            errorMapper = { it.asUiMessage() }
        ) { detailDto ->
            detailDto.toDomainModel()
        }
        emit(baseResult)
    }.catch { exception ->
        emit(
            BaseResult.Error(
                exception = exception as? Exception ?: Exception(exception),
                message = exception.asUiMessage(),
            )
        )
    }

    override fun searchMovies(query: String, page: Int): Flow<BaseResult<MoviesPage>> = flow {
        require(query.isNotBlank()) { "Search query cannot be blank" }
        require(page > 0) { "Page number must be positive, got $page" }

        val result = remoteDataSource.searchMovies(query, page)
        val baseResult = result.toBaseResult(
            errorMapper = { it.asUiMessage() }
        ) { responseDto ->
            responseDto.toDomainModel()
        }
        emit(baseResult)
    }.catch { exception ->
        emit(BaseResult.Error(
            exception = exception as? Exception ?: Exception(exception),
            message = exception.asUiMessage()
        ))
    }

    private fun Throwable.asUiMessage(): String {
        return when (this) {
            is IOException -> "Network error. Please check your internet connection."
            is SerializationException -> "Data error. We're having trouble processing the information from the server."
            is ClientRequestException -> "Invalid request. Please try again later."
            is ServerResponseException -> "Server error. Our team is working on it."
            else -> "Something went wrong. Please try again."
        }
    }
}
