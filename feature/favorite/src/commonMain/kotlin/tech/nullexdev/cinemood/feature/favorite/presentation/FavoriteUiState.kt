package tech.nullexdev.cinemood.feature.favorite.presentation

import tech.nullexdev.cinemood.core.presentation.mvi.MviUiState
import tech.nullexdev.cinemood.feature.favorite.presentation.model.FavoriteMovieItem

data class FavoriteUiState(
    val isLoading: Boolean = false,
    val favorites: List<FavoriteMovieItem> = emptyList(),
    val errorMessage: String? = null,
) : MviUiState
