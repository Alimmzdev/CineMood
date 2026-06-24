package tech.nullexdev.cinemood.feature.home.presentation

import androidx.lifecycle.viewModelScope
import tech.nullexdev.cinemood.core.domain.common.BaseResult
import tech.nullexdev.cinemood.core.presentation.mvi.MviViewModel
import tech.nullexdev.cinemood.service.domain.usecase.GetMovieDetailUseCase
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieId: Int,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
) : MviViewModel<MovieDetailUiState, MovieDetailUiAction>(
    initialState = MovieDetailUiState(),
) {
    init {
        onAction(MovieDetailUiAction.LoadDetail)
    }
    override fun onAction(action: MovieDetailUiAction) {
        when (action) {
            MovieDetailUiAction.LoadDetail, MovieDetailUiAction.Retry -> loadMovieDetail()
        }
    }
    private fun loadMovieDetail() {
        viewModelScope.launch {
            updateState {
                copy(
                    isLoading = true,
                    movieDetail = null,
                    errorMessage = null,
                )
            }
            getMovieDetailUseCase(GetMovieDetailUseCase.Params(movieId = movieId)).collect { result ->
                when (result) {
                    is BaseResult.Success -> updateState {
                        copy(
                            isLoading = false,
                            movieDetail = result.data,
                            errorMessage = null,
                        )
                    }
                    is BaseResult.Error -> updateState {
                        copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Failed to load movie details",
                        )
                    }
                }
            }
        }
    }
}
