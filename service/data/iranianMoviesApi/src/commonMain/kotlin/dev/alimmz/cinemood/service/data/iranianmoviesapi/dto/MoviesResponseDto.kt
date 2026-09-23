package dev.alimmz.cinemood.service.data.iranianmoviesapi.dto

import kotlinx.collections.immutable.toImmutableList
import dev.alimmz.cinemood.core.data.network.dto.DomainConvertible
import dev.alimmz.cinemood.service.domain.model.MoviesPage
import kotlinx.serialization.Serializable

@Serializable
data class MoviesResponseDto(
    val data: List<MovieDto>,
    val metadata: MetadataDto
) : DomainConvertible<MoviesPage> {
    override fun toDomainModel(): MoviesPage =
        MoviesPage(
            movies = data.map { it.toDomainModel() }.toImmutableList(),
            currentPage = metadata.current_page,
            perPage = metadata.per_page,
            pageCount = metadata.page_count,
            totalCount = metadata.total_count
        )
}