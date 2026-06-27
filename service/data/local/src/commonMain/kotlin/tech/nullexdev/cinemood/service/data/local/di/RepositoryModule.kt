package tech.nullexdev.cinemood.service.data.local.di

import org.koin.dsl.module
import tech.nullexdev.cinemood.service.data.local.repository.LikedVideoRepositoryImpl
import tech.nullexdev.cinemood.service.domain.repository.LikedVideoRepository

val repositoryModule = module {
    single<LikedVideoRepository> { LikedVideoRepositoryImpl(get()) }
}
