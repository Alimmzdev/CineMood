package tech.nullexdev.cinemood.feature.search.presentation

import tech.nullexdev.cinemood.core.presentation.mvi.MviUiState
import tech.nullexdev.cinemood.service.domain.moodel.Movie

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val errorMessage: String? = null,
    val hasSearched: Boolean = false,
) : MviUiState
