package com.alimmzdev.cinemood_kmp.service.domain.moodel

import com.alimmzdev.cinemood_kmp.core.domain.entity.DomainModel

data class MoviesPage(
    val movies: List<Movie>,
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