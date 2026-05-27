package tech.nullexdev.cinemood.service.data.iranianmoviesapi.di

import tech.nullexdev.cinemood.core.data.network.HttpClientFactory
import tech.nullexdev.cinemood.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSource
import tech.nullexdev.cinemood.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSourceImpl
import tech.nullexdev.cinemood.service.data.iranianmoviesapi.repository.MoviesRepositoryImpl
import tech.nullexdev.cinemood.service.domain.repository.MoviesRepository
import org.koin.dsl.module

val iranianMoviesApiDataModule = module {
    single {
        HttpClientFactory.create()
    }

    single<tech.nullexdev.cinemood.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSource> {
        _root_ide_package_.tech.nullexdev.cinemood.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSourceImpl(
            get()
        )
    }

    single<tech.nullexdev.cinemood.service.domain.repository.MoviesRepository> {
        _root_ide_package_.tech.nullexdev.cinemood.service.data.iranianmoviesapi.repository.MoviesRepositoryImpl(
            get()
        )
    }
}