package dev.alimmz.cinemood.feature.favorite.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.CancellationException
import dev.alimmz.cinemood.core.presentation.mvi.MviViewModel
import dev.alimmz.cinemood.feature.favorite.presentation.model.FavoriteMovieItem
import dev.alimmz.cinemood.service.domain.usecase.DeleteLikedVideoUseCase
import dev.alimmz.cinemood.service.domain.usecase.GetLikedVideosUseCase

class FavoriteViewModel(
    private val getLikedVideosUseCase: GetLikedVideosUseCase,
    private val deleteLikedVideoUseCase: DeleteLikedVideoUseCase,
) : MviViewModel<FavoriteUiState, FavoriteUiAction>(
    initialState = FavoriteUiState(),
) {
    private var favoritesJob: Job? = null

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
        favoritesJob?.cancel()
        favoritesJob = viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            getLikedVideosUseCase()
                .catch { e ->
                    updateState { copy(isLoading = false, errorMessage = e.message ?: "Unable to load favorites") }
                }
                .collect { likedVideos ->
                    updateState {
                        copy(
                            isLoading = false,
                            favorites = likedVideos.map { FavoriteMovieItem(
                                id = it.tmdbId,
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
        viewModelScope.launch {
            try {
                deleteLikedVideoUseCase(movie.id)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                updateState { copy(errorMessage = e.message ?: "Unable to remove favorite") }
            }
        }
    }
}
