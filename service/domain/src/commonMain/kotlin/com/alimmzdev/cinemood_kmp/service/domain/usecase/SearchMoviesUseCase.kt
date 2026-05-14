package com.alimmzdev.cinemood_kmp.service.domain.usecase

import com.alimmzdev.cinemood_kmp.core.domain.common.BaseResult
import com.alimmzdev.cinemood_kmp.core.domain.usecase.BaseUseCase
import com.alimmzdev.cinemood_kmp.service.domain.moodel.MoviesPage
import com.alimmzdev.cinemood_kmp.service.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class SearchMoviesUseCase(
    private val repository: MoviesRepository
) : BaseUseCase<SearchMoviesUseCase.Params, MoviesPage> {

    data class Params(
        val query: String,
        val page: Int = 1
    ) {
        init {
            require(query.isNotBlank()) { "Search query cannot be blank" }
            require(page > 0) { "Page number must be positive" }
        }
    }

    override fun invoke(parameters: Params): Flow<BaseResult<MoviesPage>> {
        return repository.searchMovies(parameters.query, parameters.page)
    }
}