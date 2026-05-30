package tech.nullexdev.cinemood.feature.home.presentation

import tech.nullexdev.cinemood.core.domain.presentation.mvi.MviUiAction

sealed interface HomeUiAction : MviUiAction {
    data object LoadMovies : HomeUiAction
    data object Refresh : HomeUiAction
    data object LoadNextPage : HomeUiAction
    data object SearchClicked : HomeUiAction
    data object FavoriteClicked : HomeUiAction
}
