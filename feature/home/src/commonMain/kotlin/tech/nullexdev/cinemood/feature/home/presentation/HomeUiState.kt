package tech.nullexdev.cinemood.feature.home.presentation

import tech.nullexdev.cinemood.core.presentation.mvi.MviUiState
import tech.nullexdev.cinemood.service.domain.moodel.Movie

data class HomeUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val errorMessage: String? = null,
    val currentPage: Int = 1,
    val hasNextPage: Boolean = false,
) : MviUiState
