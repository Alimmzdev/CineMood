package dev.alimmz.cinemood.feature.home.presentation

import dev.alimmz.cinemood.core.presentation.mvi.MviUiAction

sealed interface HomeUiAction : MviUiAction {
    data object LoadMovies : HomeUiAction
    data object Refresh : HomeUiAction
    data object LoadNextPage : HomeUiAction
    data object SearchClicked : HomeUiAction
    data object FavoriteClicked : HomeUiAction
}
