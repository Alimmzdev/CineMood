package tech.nullexdev.cinemood.service.domain.moodel

import tech.nullexdev.cinemood.core.domain.entity.DomainModel

data class Movie(
    val id: Int,
    val title: String,
    val poster: String,
    val genres: List<String>,
    val images: List<String>
) : DomainModel