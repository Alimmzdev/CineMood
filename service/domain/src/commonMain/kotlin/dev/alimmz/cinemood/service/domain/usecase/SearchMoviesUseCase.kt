package dev.alimmz.cinemood.service.domain.usecase

import dev.alimmz.cinemood.core.domain.common.BaseResult
import dev.alimmz.cinemood.core.domain.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow

class SearchMoviesUseCase(
    private val repository: dev.alimmz.cinemood.service.domain.repository.MoviesRepository
) : BaseUseCase<dev.alimmz.cinemood.service.domain.usecase.SearchMoviesUseCase.Params, dev.alimmz.cinemood.service.domain.model.MoviesPage> {

    data class Params(
        val query: String,
        val page: Int = 1
    ) {
        init {
            require(query.isNotBlank()) { "Search query cannot be blank" }
            require(page > 0) { "Page number must be positive" }
        }
    }

    override fun invoke(parameters: Params): Flow<BaseResult<dev.alimmz.cinemood.service.domain.model.MoviesPage>> {
        return repository.searchMovies(parameters.query, parameters.page)
    }
}