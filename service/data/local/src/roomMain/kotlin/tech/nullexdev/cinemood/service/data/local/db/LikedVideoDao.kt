package tech.nullexdev.cinemood.service.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import tech.nullexdev.cinemood.service.data.local.db.entity.LikedVideoEntity

@Dao
interface LikedVideoDao {

    @Query("SELECT * FROM liked_videos")
    fun getLikedVideos(): Flow<List<LikedVideoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikedVideo(video: LikedVideoEntity)

    @Query("DELETE FROM liked_videos WHERE tmdbId = :tmdbId")
    suspend fun deleteLikedVideo(tmdbId: Int)

    @Query("SELECT * FROM liked_videos WHERE tmdbId = :tmdbId")
    fun getLikedVideo(tmdbId: Int): Flow<LikedVideoEntity?>
}
