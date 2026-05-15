package com.alimmzdev.conemood_kmp.di

import com.alimmzdev.cinemood_kmp.service.data.iranianmoviesapi.di.iranianMoviesApiDataModule
import com.alimmzdev.cinemood_kmp.service.domain.usecase.GetMoviesUseCase
import com.alimmzdev.cinemood_kmp.service.domain.usecase.SearchMoviesUseCase
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

private val domainModule = module {
    factory<GetMoviesUseCase> { GetMoviesUseCase(get()) }
    factory<SearchMoviesUseCase> { SearchMoviesUseCase(get()) }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            iranianMoviesApiDataModule,
            domainModule,
        )
    }
}
