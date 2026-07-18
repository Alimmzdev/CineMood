package tech.nullexdev.cinemood.feature.home.di

import org.koin.core.module.dsl.viewModel
import tech.nullexdev.cinemood.feature.home.presentation.HomeViewModel
import tech.nullexdev.cinemood.feature.home.presentation.MovieDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    viewModelOf(::HomeViewModel)
    viewModel { parameters ->
        MovieDetailViewModel(
            movieId = parameters.get(),
            getMovieDetailUseCase = get(),
            insertLikedVideoUseCase = get(),
            deleteLikedVideoUseCase = get(),
            getLikedVideoUseCase = get(),
        )
    }
}
