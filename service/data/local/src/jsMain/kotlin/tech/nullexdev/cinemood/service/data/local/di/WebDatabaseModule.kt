package tech.nullexdev.cinemood.service.data.local.di

import app.cash.sqldelight.driver.worker.WebWorkerDriver
import org.koin.dsl.module
import tech.nullexdev.cinemood.service.data.local.LikedVideoDataSource
import tech.nullexdev.cinemood.service.data.local.SqlDelightLikedVideoDataSource
import tech.nullexdev.cinemood.service.data.local.db.CineMoodDatabase

private fun createModuleWorker(url: String): org.w3c.dom.Worker {
    return js("new Worker(url, {type:'module'})") as org.w3c.dom.Worker
}

val webDatabaseModule = module {
    single {
        val driver = WebWorkerDriver(
            createModuleWorker("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js")
        )
        CineMoodDatabase(driver)
    }
    single<LikedVideoDataSource> { SqlDelightLikedVideoDataSource(get()) }
}
