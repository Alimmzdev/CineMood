package dev.alimmz.cinemood.service.domain.usecase

import dev.alimmz.cinemood.core.domain.common.BaseResult
import dev.alimmz.cinemood.core.domain.usecase.BaseUseCase
import dev.alimmz.cinemood.service.domain.model.MovieDetail
import dev.alimmz.cinemood.service.domain.repository.MoviesRepository
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
