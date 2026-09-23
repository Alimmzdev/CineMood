package dev.alimmz.cinemood.service.domain.usecase

import dev.alimmz.cinemood.core.domain.common.BaseResult
import dev.alimmz.cinemood.core.domain.usecase.BaseUseCase
import dev.alimmz.cinemood.service.domain.model.MoviesPage
import dev.alimmz.cinemood.service.domain.repository.MoviesRepository
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