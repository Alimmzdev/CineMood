package dev.alimmz.cinemood.di

import dev.alimmz.cinemood.service.data.local.di.localDataModule
import dev.alimmz.cinemood.service.domain.di.useCaseModule
import dev.alimmz.cinemood.feature.favorite.di.favoriteModule
import dev.alimmz.cinemood.feature.home.di.homeModule
import dev.alimmz.cinemood.feature.search.di.searchModule
import dev.alimmz.cinemood.feature.settings.di.settingsModule
import dev.alimmz.cinemood.presentation.app.AppViewModel
import dev.alimmz.cinemood.service.data.iranianmoviesapi.di.iranianMoviesApiDataModule
import dev.alimmz.cinemood.service.domain.usecase.GetMovieDetailUseCase
import dev.alimmz.cinemood.service.domain.usecase.GetMoviesUseCase
import dev.alimmz.cinemood.service.domain.usecase.SearchMoviesUseCase
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import dev.alimmz.cinemood.core.data.di.coreDataModule
import dev.alimmz.cinemood.service.domain.usecase.GetLikedVideoUseCase

private val presentationModule = module {
    viewModelOf(::AppViewModel)
}

private val domainModule = module {
    factory<GetMoviesUseCase> {
        GetMoviesUseCase(
            get()
        )
    }
    factory<SearchMoviesUseCase> {
        SearchMoviesUseCase(
            get()
        )
    }
    factory<GetMovieDetailUseCase> {
        GetMovieDetailUseCase(
            get()
        )
    }
    factory<GetLikedVideoUseCase> {
        GetLikedVideoUseCase(
            get()
        )
    }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            coreDataModule,
            localDataModule,
            iranianMoviesApiDataModule,
            domainModule,
            useCaseModule,
            presentationModule,
            homeModule,
            searchModule,
            favoriteModule,
            settingsModule,
        )
    }
}
