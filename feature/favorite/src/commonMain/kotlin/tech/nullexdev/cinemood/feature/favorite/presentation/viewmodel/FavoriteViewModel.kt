package tech.nullexdev.cinemood.feature.favorite.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import tech.nullexdev.cinemood.service.domain.usecase.GetLikedVideosUseCase

class FavoriteViewModel(
    getLikedVideosUseCase: GetLikedVideosUseCase
) : ViewModel() {

    val likedVideos = getLikedVideosUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
