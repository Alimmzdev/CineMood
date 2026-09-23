package dev.alimmz.cinemood.feature.favorite.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import dev.alimmz.cinemood.feature.favorite.presentation.FavoriteViewModel

val favoriteModule = module {
    viewModel {
        FavoriteViewModel(
            getLikedVideosUseCase = get(),
            deleteLikedVideoUseCase = get(),
        )
    }
}
