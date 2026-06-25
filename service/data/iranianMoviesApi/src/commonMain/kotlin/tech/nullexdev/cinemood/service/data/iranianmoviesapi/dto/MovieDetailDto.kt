package tech.nullexdev.cinemood.service.data.iranianmoviesapi.dto

import tech.nullexdev.cinemood.core.data.network.dto.DomainConvertible
import tech.nullexdev.cinemood.service.data.iranianmoviesapi.util.MoviesApiUrlResolver
import tech.nullexdev.cinemood.service.domain.model.MovieDetail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailDto(
    val id: Int,
    val title: String,
    val poster: String,
    val year: String = "",
    val rated: String = "",
    val released: String = "",
    val runtime: String = "",
    val director: String = "",
    val writer: String = "",
    val actors: String = "",
    val plot: String = "",
    val country: String = "",
    val awards: String = "",
    val metascore: String = "",
    @SerialName("imdb_rating")
    val imdbRating: String = "",
    @SerialName("imdb_votes")
    val imdbVotes: String = "",
    @SerialName("imdb_id")
    val imdbId: String = "",
    val type: String = "",
    val genres: List<String> = emptyList(),
    val images: List<String> = emptyList(),
) : DomainConvertible<MovieDetail> {
    override fun toDomainModel(): MovieDetail {
        return MovieDetail(
            id = id,
            title = title,
            poster = MoviesApiUrlResolver.resolveImageUrl(poster),
            year = year,
            rated = rated,
            released = released,
            runtime = runtime,
            director = director,
            writer = writer,
            actors = actors,
            plot = plot,
            country = country,
            awards = awards.decodeHtmlEntities(),
            metascore = metascore,
            imdbRating = imdbRating,
            imdbVotes = imdbVotes,
            imdbId = imdbId,
            type = type,
            genres = genres,
            images = images.map(MoviesApiUrlResolver::resolveImageUrl),
        )
    }
}

private fun String.decodeHtmlEntities(): String {
    return replace("&amp;", "&")
}
