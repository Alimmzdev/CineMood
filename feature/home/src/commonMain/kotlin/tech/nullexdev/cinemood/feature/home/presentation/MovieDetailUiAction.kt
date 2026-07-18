package tech.nullexdev.cinemood.feature.home.presentation

import tech.nullexdev.cinemood.core.presentation.mvi.MviUiAction

sealed interface MovieDetailUiAction : MviUiAction {
    data object LoadDetail : MovieDetailUiAction
    data object Retry : MovieDetailUiAction
    data class ToggleLike(
        val movieId: Int,
        val title: String,
        val posterUrl: String,
    ) : MovieDetailUiAction
}
