package dev.alimmz.cinemood.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {
    @Serializable
    data object Home : Screen
    @Serializable
    data object Search : Screen
    @Serializable
    data object Favorite : Screen
    @Serializable
    data object Settings : Screen

    @Serializable
    data class MovieDetail(
        val movieId: Int,
        val movieTitle: String,
        val moviePoster: String,
        val posterCornerRadiusDp: Int = 24,
    ) : Screen
}
