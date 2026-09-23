package dev.alimmz.cinemood.service.domain.model

import dev.alimmz.cinemood.core.domain.entity.DomainModel

data class MovieDetail(
    val id: Int,
    val title: String,
    val poster: String,
    val year: String,
    val rated: String,
    val released: String,
    val runtime: String,
    val director: String,
    val writer: String,
    val actors: String,
    val plot: String,
    val country: String,
    val awards: String,
    val metascore: String,
    val imdbRating: String,
    val imdbVotes: String,
    val imdbId: String,
    val type: String,
    val genres: List<String>,
    val images: List<String>,
) : DomainModel
