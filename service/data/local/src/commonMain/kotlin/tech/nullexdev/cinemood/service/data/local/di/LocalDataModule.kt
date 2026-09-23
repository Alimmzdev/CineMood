package tech.nullexdev.cinemood.service.data.local.di

import org.koin.core.module.Module
import org.koin.dsl.module
import tech.nullexdev.cinemood.service.data.local.repository.LikedVideoRepositoryImpl
import tech.nullexdev.cinemood.service.domain.repository.LikedVideoRepository

expect fun platformModule(): Module

val repositoryModule = module {
    single<LikedVideoRepository> { LikedVideoRepositoryImpl(get()) }
}

val localDataModule = module {
    includes(repositoryModule, platformModule())
}
