package tech.nullexdev.cinemood.service.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import tech.nullexdev.cinemood.service.data.local.db.entity.LikedVideoEntity

@Database(entities = [LikedVideoEntity::class], version = 1)
abstract class CineMookDatabase : RoomDatabase() {
    abstract fun likedVideoDao(): LikedVideoDao
}
