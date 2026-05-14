package com.alimmzdev.cinemood_kmp.service.data.iranianmoviesapi.di

import com.alimmzdev.cinemood_kmp.core.data.network.HttpClientFactory
import com.alimmzdev.cinemood_kmp.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSource
import com.alimmzdev.cinemood_kmp.service.data.iranianmoviesapi.datasource.MoviesRemoteDataSourceImpl
import com.alimmzdev.cinemood_kmp.service.data.iranianmoviesapi.repository.MoviesRepositoryImpl
import com.alimmzdev.cinemood_kmp.service.domain.repository.MoviesRepository
import org.koin.dsl.module

val moviesApiModule = module {
    single {
        HttpClientFactory.create()
    }

    single<MoviesRemoteDataSource> {
        MoviesRemoteDataSourceImpl(get())
    }

    single<MoviesRepository> {
        MoviesRepositoryImpl(get())
    }
}