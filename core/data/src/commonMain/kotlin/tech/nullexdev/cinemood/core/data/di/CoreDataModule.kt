package tech.nullexdev.cinemood.core.data.di

import org.koin.dsl.module
import tech.nullexdev.cinemood.core.data.repository.ThemeRepositoryImpl
import tech.nullexdev.cinemood.core.domain.repository.ThemeRepository

val coreDataModule = module {
    single<ThemeRepository> { ThemeRepositoryImpl() }
}
