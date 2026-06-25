package tech.nullexdev.cinemood.feature.favorite.presentation.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class FavoriteMovieItem(
    val id: Int,
    val title: String,
    val poster: String = "",
    val genres: ImmutableList<String> = persistentListOf(),
)
