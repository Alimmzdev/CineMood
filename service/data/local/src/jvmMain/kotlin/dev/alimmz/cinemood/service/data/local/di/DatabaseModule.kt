package dev.alimmz.cinemood.service.data.local.di

import androidx.room.Room
import org.koin.dsl.module
import dev.alimmz.cinemood.service.data.local.db.CineMookDatabase
import dev.alimmz.cinemood.service.data.local.LikedVideoDataSource
import dev.alimmz.cinemood.service.data.local.RoomLikedVideoDataSource
import java.io.File

val databaseModule = module {
    single {
        val dbFile = File(System.getProperty("user.home"), "cinemook.db")
        Room.databaseBuilder<CineMookDatabase>(
            dbFile.absolutePath,
        ).build()
    }

    single { get<CineMookDatabase>().likedVideoDao() }
    single<LikedVideoDataSource> { RoomLikedVideoDataSource(get()) }
}
