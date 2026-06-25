package tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto

import kotlinx.collections.immutable.toImmutableList
import tech.nullexdev.cinemood.core.data.network.dto.DomainConvertible
import tech.nullexdev.cinemood.service.domain.model.Movie
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