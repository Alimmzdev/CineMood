package tech.nullexdev.cinemood.service.data.local.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import tech.nullexdev.cinemood.service.data.local.db.entity.LikedVideoEntity

@ConstructedBy(CineMookDatabaseConstructor::class)
@Database(entities = [LikedVideoEntity::class], version = 1)
abstract class CineMookDatabase : RoomDatabase() {
    abstract fun likedVideoDao(): LikedVideoDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object CineMookDatabaseConstructor : RoomDatabaseConstructor<CineMookDatabase> {
    override fun initialize(): CineMookDatabase
}
