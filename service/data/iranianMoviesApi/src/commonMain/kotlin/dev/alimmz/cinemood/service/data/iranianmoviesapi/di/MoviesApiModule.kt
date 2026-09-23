package dev.alimmz.cinemood.service.data.iranianmoviesapi.di

import dev.alimmz.cinemood.core.data.network.HttpClientFactory
import dev.alimmz.cinemood.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSource
import dev.alimmz.cinemood.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSourceImpl
import dev.alimmz.cinemood.service.data.iranianmoviesapi.repository.MoviesRepositoryImpl
import dev.alimmz.cinemood.service.domain.repository.MoviesRepository
import org.koin.dsl.module

val iranianMoviesApiDataModule = module {
    single {
        HttpClientFactory.create()
    }

    single<MoviesRemoteDataSource> {
        MoviesRemoteDataSourceImpl(
            get()
        )
    }

    single<MoviesRepository> {
        MoviesRepositoryImpl(
            get()
        )
    }
}