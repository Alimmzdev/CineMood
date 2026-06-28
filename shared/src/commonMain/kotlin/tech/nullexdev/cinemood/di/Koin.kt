package tech.nullexdev.cinemood.di

import tech.nullexdev.cinemood.service.data.local.di.localDataModule
import tech.nullexdev.cinemood.service.domain.di.useCaseModule
import tech.nullexdev.cinemood.feature.favorite.di.favoriteModule
import tech.nullexdev.cinemood.feature.home.di.homeModule
import tech.nullexdev.cinemood.feature.search.di.searchModule
import tech.nullexdev.cinemood.feature.settings.di.settingsModule
import tech.nullexdev.cinemood.presentation.app.AppViewModel
import tech.nullexdev.cinemood.service.data.iranianmoviesapi.di.iranianMoviesApiDataModule
import tech.nullexdev.cinemood.service.domain.usecase.GetMovieDetailUseCase
import tech.nullexdev.cinemood.service.domain.usecase.GetMoviesUseCase
import tech.nullexdev.cinemood.service.domain.usecase.SearchMoviesUseCase
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import tech.nullexdev.cinemood.core.data.di.coreDataModule
import tech.nullexdev.cinemood.service.domain.usecase.GetLikedVideoUseCase

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
