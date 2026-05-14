package com.alimmzdev.cinemood_kmp.service.domain.usecase

import com.alimmzdev.cinemood_kmp.core.domain.common.BaseResult
import com.alimmzdev.cinemood_kmp.core.domain.usecase.BaseUseCase
import com.alimmzdev.cinemood_kmp.service.domain.moodel.MoviesPage
import com.alimmzdev.cinemood_kmp.service.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class GetMoviesUseCase(
    private val repository: MoviesRepository
) : BaseUseCase<GetMoviesUseCase.Params, MoviesPage> {

    data class Params(
        val page: Int = 1
    ) {
        init {
            require(page > 0) { "Page number must be positive" }
        }
    }

    override fun invoke(parameters: Params): Flow<BaseResult<MoviesPage>> {
        return repository.getMovies(parameters.page)
    }

}