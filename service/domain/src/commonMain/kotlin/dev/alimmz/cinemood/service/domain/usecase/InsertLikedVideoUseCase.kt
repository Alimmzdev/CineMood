package dev.alimmz.cinemood.service.domain.usecase

import dev.alimmz.cinemood.service.domain.entity.LikedVideo
import dev.alimmz.cinemood.service.domain.repository.LikedVideoRepository

class InsertLikedVideoUseCase(private val repository: LikedVideoRepository) {
    suspend operator fun invoke(video: LikedVideo) {
        repository.insertLikedVideo(video)
    }
}
