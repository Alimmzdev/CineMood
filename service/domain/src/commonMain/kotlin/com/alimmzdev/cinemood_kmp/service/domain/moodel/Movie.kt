package com.alimmzdev.cinemood_kmp.service.domain.moodel

import com.alimmzdev.cinemood_kmp.core.domain.entity.DomainModel

data class Movie(
    val id: Int,
    val title: String,
    val poster: String,
    val genres: List<String>,
    val images: List<String>
) : DomainModel