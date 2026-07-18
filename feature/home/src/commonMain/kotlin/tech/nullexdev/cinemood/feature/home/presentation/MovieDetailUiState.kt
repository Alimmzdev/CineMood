package tech.nullexdev.cinemood.feature.home.presentation

import tech.nullexdev.cinemood.core.presentation.mvi.MviUiState
import tech.nullexdev.cinemood.service.domain.model.MovieDetail

data class MovieDetailUiState(
    val isLoading: Boolean = false,
    val movieDetail: MovieDetail? = null,
    val isLiked: Boolean = false,
    val errorMessage: String? = null,
) : MviUiState
