package dev.alimmz.cinemood.service.data.local.di

import org.koin.core.module.Module
import org.koin.dsl.module
import dev.alimmz.cinemood.service.data.local.repository.LikedVideoRepositoryImpl
import dev.alimmz.cinemood.service.domain.repository.LikedVideoRepository

expect fun platformModule(): Module

val repositoryModule = module {
    single<LikedVideoRepository> { LikedVideoRepositoryImpl(get()) }
}

val localDataModule = module {
    includes(repositoryModule, platformModule())
}
