package com.alimmzdev.cinemood_kmp.core.data.network.dto

import com.alimmzdev.cinemood_kmp.core.domain.entity.DomainModel

interface DomainConvertible<out T: DomainModel> {
    fun toDomainModel(): T
}