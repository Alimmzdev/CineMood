package tech.nullexdev.cinemood.feature.favorite.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import tech.nullexdev.cinemood.core.presentation.mvi.MviViewModel
import tech.nullexdev.cinemood.feature.favorite.presentation.model.FavoriteMovieItem
import tech.nullexdev.cinemood.service.domain.usecase.GetLikedVideosUseCase

class FavoriteViewModel(
    private val getLikedVideosUseCase: GetLikedVideosUseCase
) : MviViewModel<FavoriteUiState, FavoriteUiAction>(
    initialState = FavoriteUiState(),
) {
    init {
        onAction(FavoriteUiAction.LoadFavorites)
    }

    override fun onAction(action: FavoriteUiAction) {
        when (action) {
            FavoriteUiAction.LoadFavorites, FavoriteUiAction.Refresh -> loadFavorites()
            is FavoriteUiAction.RemoveFavorite -> removeFavorite(action.movie)
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            getLikedVideosUseCase()
                .catch { e ->
                    updateState { copy(isLoading = false, errorMessage = e.message) }
                }
                .collect { likedVideos ->
                    updateState {
                        copy(
                            isLoading = false,
                            favorites = likedVideos.map { FavoriteMovieItem(
                                id = it.id,
                                title = it.title,
                                poster = it.posterUrl,
                                genres = persistentListOf()
                            ) },
                            errorMessage = null,
                        )
                    }
                }
        }
    }

    private fun removeFavorite(movie: FavoriteMovieItem) {
        updateState {
            copy(favorites = favorites.filterNot { it.id == movie.id })
        }
    }
}