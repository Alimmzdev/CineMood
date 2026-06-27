package tech.nullexdev.cinemood.service.data.local.di

import org.koin.dsl.module

val localDataModule = module {
    includes(platformModule(), repositoryModule)
}
