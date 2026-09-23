package dev.alimmz.cinemood.feature.favorite.presentation

import dev.alimmz.cinemood.core.presentation.mvi.MviUiState
import dev.alimmz.cinemood.feature.favorite.presentation.model.FavoriteMovieItem

data class FavoriteUiState(
    val isLoading: Boolean = false,
    val favorites: List<FavoriteMovieItem> = emptyList(),
    val errorMessage: String? = null,
) : MviUiState
