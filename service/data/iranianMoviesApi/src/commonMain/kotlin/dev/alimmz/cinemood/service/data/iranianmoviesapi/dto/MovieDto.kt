package dev.alimmz.cinemood.service.data.iranianmoviesapi.dto

import kotlinx.collections.immutable.toImmutableList
import dev.alimmz.cinemood.core.data.network.dto.DomainConvertible
import dev.alimmz.cinemood.service.domain.model.Movie
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    val id: Int,
    val title: String,
    val poster: String,
    val genres: List<String> = emptyList(),
    val images: List<String> = emptyList()
) : DomainConvertible<Movie> {
    override fun toDomainModel(): Movie =
        Movie(
            id = id,
            title = title,
            poster = poster,
            genres = genres.toImmutableList(),
            images = images.toImmutableList()
        )
}