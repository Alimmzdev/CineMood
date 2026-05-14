package com.alimmzdev.cinemood_kmp.service.data.iranianmoviesapi.dto

import com.alimmzdev.cinemood_kmp.core.data.network.dto.DomainConvertible
import com.alimmzdev.cinemood_kmp.service.domain.moodel.Movie
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    val id: Int,
    val title: String,
    val poster: String,
    val genres: List<String>,
    val images: List<String>
) : DomainConvertible<Movie> {
    override fun toDomainModel(): Movie = Movie(
        id = id,
        title = title,
        poster = poster,
        genres = genres,
        images = images
    )
}