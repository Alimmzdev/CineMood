package tech.nullexdev.cinemood.service.data.local.di

import org.koin.dsl.module
import tech.nullexdev.cinemood.service.data.local.LikedVideoDataSource
import tech.nullexdev.cinemood.service.data.local.SqlDelightLikedVideoDataSource
import tech.nullexdev.cinemood.service.data.local.db.CineMoodDatabase
import app.cash.sqldelight.driver.native.NativeSqliteDriver

val databaseModule = module {
    single {
        val driver = NativeSqliteDriver(CineMoodDatabase.Schema, "cinemook.db")
        CineMoodDatabase(driver)
    }
    single<LikedVideoDataSource> { SqlDelightLikedVideoDataSource(get()) }
}
