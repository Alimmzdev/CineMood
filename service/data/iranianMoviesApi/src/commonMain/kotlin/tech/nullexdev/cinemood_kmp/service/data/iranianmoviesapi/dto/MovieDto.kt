package tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto

import tech.nullexdev.cinemood.core.data.network.dto.DomainConvertible
import tech.nullexdev.cinemood.service.domain.moodel.Movie
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    val id: Int,
    val title: String,
    val poster: String,
    val genres: List<String>,
    val images: List<String>
) : DomainConvertible<tech.nullexdev.cinemood.service.domain.moodel.Movie> {
    override fun toDomainModel(): tech.nullexdev.cinemood.service.domain.moodel.Movie =
        _root_ide_package_.tech.nullexdev.cinemood.service.domain.moodel.Movie(
            id = id,
            title = title,
            poster = poster,
            genres = genres,
            images = images
        )
}