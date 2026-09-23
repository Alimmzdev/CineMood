package dev.alimmz.cinemood.core.data.network.dto

import dev.alimmz.cinemood.core.domain.entity.DomainModel

interface DomainConvertible<out T: dev.alimmz.cinemood.core.domain.entity.DomainModel> {
    fun toDomainModel(): T
}