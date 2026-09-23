package dev.alimmz.cinemood.feature.favorite.presentation

import dev.alimmz.cinemood.core.presentation.mvi.MviUiAction
import dev.alimmz.cinemood.feature.favorite.presentation.model.FavoriteMovieItem

sealed interface FavoriteUiAction : MviUiAction {
    data object LoadFavorites : FavoriteUiAction
    data object Refresh : FavoriteUiAction
    data class RemoveFavorite(val movie: FavoriteMovieItem) : FavoriteUiAction
}
