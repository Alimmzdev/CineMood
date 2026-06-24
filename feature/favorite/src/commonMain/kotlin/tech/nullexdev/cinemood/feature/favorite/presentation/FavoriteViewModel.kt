package tech.nullexdev.cinemood.feature.favorite.presentation

import androidx.lifecycle.viewModelScope
import tech.nullexdev.cinemood.core.presentation.mvi.MviViewModel
import tech.nullexdev.cinemood.feature.favorite.presentation.model.FavoriteMovieItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoriteViewModel : MviViewModel<FavoriteUiState, FavoriteUiAction>(
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
            delay(300)
            updateState {
                copy(
                    isLoading = false,
                    favorites = placeholderFavorites(),
                    errorMessage = null,
                )
            }
        }
    }
    private fun removeFavorite(movie: FavoriteMovieItem) {
        updateState {
            copy(favorites = favorites.filterNot { it.id == movie.id })
        }
    }
    private fun placeholderFavorites(): List<FavoriteMovieItem> {
        return listOf(
            FavoriteMovieItem(
                id = 1,
                title = "Inception",
                poster = "https://image.tmdb.org/t/p/w500/9gk7Fn9sVAsS9Te6B1pU3O9sbUC.jpg",
                genres = listOf("Action", "Sci-Fi")
            ),
            FavoriteMovieItem(
                id = 2,
                title = "The Dark Knight",
                poster = "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDp92SMRYwT6C7R2R9V.jpg",
                genres = listOf("Action", "Crime")
            ),
        )
    }
}
