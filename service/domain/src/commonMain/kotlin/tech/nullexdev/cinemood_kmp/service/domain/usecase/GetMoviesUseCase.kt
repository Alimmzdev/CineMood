package tech.nullexdev.cinemood.service.domain.usecase

import tech.nullexdev.cinemood.core.domain.common.BaseResult
import tech.nullexdev.cinemood.core.domain.usecase.BaseUseCase
import tech.nullexdev.cinemood.service.domain.moodel.MoviesPage
import tech.nullexdev.cinemood.service.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class GetMoviesUseCase(
    private val repository: tech.nullexdev.cinemood.service.domain.repository.MoviesRepository
) : BaseUseCase<tech.nullexdev.cinemood.service.domain.usecase.GetMoviesUseCase.Params, tech.nullexdev.cinemood.service.domain.moodel.MoviesPage> {

    data class Params(
        val page: Int = 1
    ) {
        init {
            require(page > 0) { "Page number must be positive" }
        }
    }

    override fun invoke(parameters: Params): Flow<BaseResult<tech.nullexdev.cinemood.service.domain.moodel.MoviesPage>> {
        return repository.getMovies(parameters.page)
    }

}