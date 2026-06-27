package tech.nullexdev.cinemood.feature.favorite.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tech.nullexdev.cinemood.feature.favorite.presentation.viewmodel.FavoriteViewModel

val favoriteModule = module {
    viewModel { FavoriteViewModel(get()) }
}
