package tech.nullexdev.cinemood.feature.home.presentation

import androidx.lifecycle.viewModelScope
import tech.nullexdev.cinemood.core.domain.common.BaseResult
import tech.nullexdev.cinemood.core.presentation.mvi.MviViewModel
import tech.nullexdev.cinemood.service.domain.entity.LikedVideo
import tech.nullexdev.cinemood.service.domain.usecase.DeleteLikedVideoUseCase
import tech.nullexdev.cinemood.service.domain.usecase.GetLikedVideoUseCase
import tech.nullexdev.cinemood.service.domain.usecase.GetMovieDetailUseCase
import tech.nullexdev.cinemood.service.domain.usecase.InsertLikedVideoUseCase
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieId: Int,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val insertLikedVideoUseCase: InsertLikedVideoUseCase,
    private val deleteLikedVideoUseCase: DeleteLikedVideoUseCase,
    private val getLikedVideoUseCase: GetLikedVideoUseCase,
) : MviViewModel<MovieDetailUiState, MovieDetailUiAction>(
    initialState = MovieDetailUiState(),
) {
    init {
        onAction(MovieDetailUiAction.LoadDetail)
    }
    override fun onAction(action: MovieDetailUiAction) {
        when (action) {
            MovieDetailUiAction.LoadDetail, MovieDetailUiAction.Retry -> loadMovieDetail()
            is MovieDetailUiAction.ToggleLike -> toggleLike(action)
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
        viewModelScope.launch {
            getLikedVideoUseCase(movieId).collect { liked ->
                updateState { copy(isLiked = liked != null) }
            }
        }
    }
    private fun toggleLike(action: MovieDetailUiAction.ToggleLike) {
        viewModelScope.launch {
            if (currentState.isLiked) {
                deleteLikedVideoUseCase(movieId)
            } else {
                insertLikedVideoUseCase(
                    LikedVideo(
                        id = 0,
                        title = action.title,
                        posterUrl = action.posterUrl,
                        tmdbId = action.movieId,
                    )
                )
            }
        }
    }
}
