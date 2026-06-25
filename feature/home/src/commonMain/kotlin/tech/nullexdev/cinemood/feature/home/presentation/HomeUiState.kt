package tech.nullexdev.cinemood.feature.home.presentation

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import tech.nullexdev.cinemood.core.presentation.mvi.MviUiState
import tech.nullexdev.cinemood.service.domain.model.Movie

data class HomeUiState(
    val isLoading: Boolean = false,
    val movies: ImmutableList<Movie> = persistentListOf(),
    val errorMessage: String? = null,
    val currentPage: Int = 1,
    val hasNextPage: Boolean = false,
) : MviUiState
