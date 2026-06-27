package tech.nullexdev.cinemood.feature.favorite.di

import org.koin.dsl.module
import tech.nullexdev.cinemood.feature.favorite.presentation.FavoriteViewModel

val favoriteModule = module {
    factory { FavoriteViewModel(get()) }
}
