package dev.alimmz.cinemood.service.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liked_videos")
data class LikedVideoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val posterUrl: String,
    val tmdbId: Int
)
