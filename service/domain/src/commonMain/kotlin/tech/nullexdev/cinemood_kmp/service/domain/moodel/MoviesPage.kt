package tech.nullexdev.cinemood.service.domain.moodel

import tech.nullexdev.cinemood.core.domain.entity.DomainModel

data class MoviesPage(
    val movies: List<tech.nullexdev.cinemood.service.domain.moodel.Movie>,
    val currentPage: Int,
    val perPage: Int,
    val pageCount: Int,
    val totalCount: Int
) : DomainModel {

    val hasNextPage: Boolean
        get() = currentPage < pageCount

    val hasPreviousPage: Boolean
        get() = currentPage > 1

    val isEmpty: Boolean
        get() = movies.isEmpty()

    val size: Int
        get() = movies.size
}