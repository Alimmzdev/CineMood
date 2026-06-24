package tech.nullexdev.cinemood.feature.favorite.presentation

import tech.nullexdev.cinemood.core.presentation.mvi.MviUiAction
import tech.nullexdev.cinemood.feature.favorite.presentation.model.FavoriteMovieItem

sealed interface FavoriteUiAction : MviUiAction {
    data object LoadFavorites : FavoriteUiAction
    data object Refresh : FavoriteUiAction
    data class RemoveFavorite(val movie: FavoriteMovieItem) : FavoriteUiAction
}
