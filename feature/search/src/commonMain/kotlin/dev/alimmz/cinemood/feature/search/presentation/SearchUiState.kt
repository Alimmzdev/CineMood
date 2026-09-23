package dev.alimmz.cinemood.feature.search.presentation

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import dev.alimmz.cinemood.core.presentation.mvi.MviUiState
import dev.alimmz.cinemood.service.domain.model.Movie

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val movies: ImmutableList<Movie> = persistentListOf(),
    val errorMessage: String? = null,
    val hasSearched: Boolean = false,
) : MviUiState
