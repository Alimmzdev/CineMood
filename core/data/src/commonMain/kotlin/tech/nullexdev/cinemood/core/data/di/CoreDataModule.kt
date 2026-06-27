package tech.nullexdev.cinemood.core.data.di

import tech.nullexdev.cinemood.core.data.repository.ThemeRepositoryImpl
import tech.nullexdev.cinemood.core.domain.repository.ThemeRepository
import tech.nullexdev.cinemood.core.data.db.CineMoodDatabase
import tech.nullexdev.cinemood.core.data.db.DriverFactory
import org.koin.dsl.module
import org.koin.core.module.Module

expect fun platformModule(): Module

val coreDataModule = module {
    includes(platformModule())
    single<ThemeRepository> { ThemeRepositoryImpl() }
}
