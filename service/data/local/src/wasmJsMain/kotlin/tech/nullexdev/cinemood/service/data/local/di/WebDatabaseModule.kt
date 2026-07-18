package tech.nullexdev.cinemood.service.data.local.di

import app.cash.sqldelight.driver.worker.WebWorkerDriver
import org.koin.dsl.module
import tech.nullexdev.cinemood.service.data.local.LikedVideoDataSource
import tech.nullexdev.cinemood.service.data.local.SqlDelightLikedVideoDataSource
import tech.nullexdev.cinemood.service.data.local.db.CineMoodDatabase
import web.workers.Worker

val webDatabaseModule = module {
    single {
        //TODO: Fix Wasm Worker Issue
        val driver = WebWorkerDriver(
            Worker("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js")
        )

        CineMoodDatabase.Schema.create(driver).await()

        CineMoodDatabase(driver)
    }
    single<LikedVideoDataSource> { SqlDelightLikedVideoDataSource(get()) }
}
