package dev.alimmz.cinemood.core.data.di

import org.koin.dsl.module
import dev.alimmz.cinemood.core.data.repository.ThemeRepositoryImpl
import dev.alimmz.cinemood.core.domain.repository.ThemeRepository

val coreDataModule = module {
    single<ThemeRepository> { ThemeRepositoryImpl() }
}
