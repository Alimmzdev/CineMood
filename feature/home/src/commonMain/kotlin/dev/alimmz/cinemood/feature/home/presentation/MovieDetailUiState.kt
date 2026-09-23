package dev.alimmz.cinemood.feature.home.presentation

import dev.alimmz.cinemood.core.presentation.mvi.MviUiState
import dev.alimmz.cinemood.service.domain.model.MovieDetail

data class MovieDetailUiState(
    val isLoading: Boolean = false,
    val movieDetail: MovieDetail? = null,
    val isLiked: Boolean = false,
    val errorMessage: String? = null,
) : MviUiState
