package tech.nullexdev.cinemood.service.domain.usecase

import tech.nullexdev.cinemood.core.domain.common.BaseResult
import tech.nullexdev.cinemood.core.domain.usecase.BaseUseCase
import tech.nullexdev.cinemood.service.domain.model.MovieDetail
import tech.nullexdev.cinemood.service.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class GetMovieDetailUseCase(
    private val repository: MoviesRepository,
) : BaseUseCase<GetMovieDetailUseCase.Params, MovieDetail> {
    data class Params(
        val movieId: Int,
    ) {
        init {
            require(movieId > 0) { "Movie id must be positive" }
        }
    }
    override fun invoke(parameters: Params): Flow<BaseResult<MovieDetail>> {
        return repository.getMovieDetail(parameters.movieId)
    }
}
