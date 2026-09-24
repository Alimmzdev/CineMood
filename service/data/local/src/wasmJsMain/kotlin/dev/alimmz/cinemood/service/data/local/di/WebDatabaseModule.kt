package dev.alimmz.cinemood.service.data.local.di

import org.koin.dsl.module
import dev.alimmz.cinemood.service.data.local.LikedVideoDataSource
import dev.alimmz.cinemood.service.data.local.SqlDelightLikedVideoDataSource

val webDatabaseModule = module {
    single<LikedVideoDataSource> { SqlDelightLikedVideoDataSource() }
}
