package tech.nullexdev.cinemood.core.data.network.dto

import tech.nullexdev.cinemood.core.domain.entity.DomainModel

interface DomainConvertible<out T: tech.nullexdev.cinemood.core.domain.entity.DomainModel> {
    fun toDomainModel(): T
}