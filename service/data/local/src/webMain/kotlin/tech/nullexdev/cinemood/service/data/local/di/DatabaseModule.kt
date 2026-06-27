package tech.nullexdev.cinemood.service.data.local.di

import org.koin.dsl.module
import tech.nullexdev.cinemood.service.data.local.LikedVideoDataSource
import tech.nullexdev.cinemood.service.data.local.SqlDelightLikedVideoDataSource
import tech.nullexdev.cinemood.service.data.local.db.CineMoodDatabase
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import org.w3c.dom.Worker

val databaseModule = module {
    single {
        val driver = WebWorkerDriver(
            Worker(
                js("""
                    new URL("@js-sd/sql.js/dist/worker.sql-wasm.js", import.meta.url)
                """)
            )
        )
        CineMoodDatabase(driver)
    }
    single<LikedVideoDataSource> { SqlDelightLikedVideoDataSource(get()) }
}
