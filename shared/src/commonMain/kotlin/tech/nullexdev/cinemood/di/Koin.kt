package tech.nullexdev.cinemood.di

import tech.nullexdev.cinemood.service.data.iranianmoviesapi.di.iranianMoviesApiDataModule
import tech.nullexdev.cinemood.service.domain.usecase.GetMoviesUseCase
import tech.nullexdev.cinemood.service.domain.usecase.SearchMoviesUseCase
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

private val domainModule = module {
    factory<tech.nullexdev.cinemood.service.domain.usecase.GetMoviesUseCase> {
        _root_ide_package_.tech.nullexdev.cinemood.service.domain.usecase.GetMoviesUseCase(
            get()
        )
    }
    factory<tech.nullexdev.cinemood.service.domain.usecase.SearchMoviesUseCase> {
        _root_ide_package_.tech.nullexdev.cinemood.service.domain.usecase.SearchMoviesUseCase(
            get()
        )
    }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            _root_ide_package_.tech.nullexdev.cinemood.service.data.iranianmoviesapi.di.iranianMoviesApiDataModule,
            _root_ide_package_.tech.nullexdev.cinemood.di.domainModule,
        )
    }
}
