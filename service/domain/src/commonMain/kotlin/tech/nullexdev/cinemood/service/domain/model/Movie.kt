package tech.nullexdev.cinemood.service.domain.model

import kotlinx.collections.immutable.ImmutableList
import tech.nullexdev.cinemood.core.domain.entity.DomainModel

data class Movie(
    val id: Int,
    val title: String,
    val poster: String,
    val genres: ImmutableList<String>,
    val images: ImmutableList<String>
) : DomainModel