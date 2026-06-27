package tech.nullexdev.cinemood.service.domain.usecase

import tech.nullexdev.cinemood.service.domain.entity.LikedVideo
import tech.nullexdev.cinemood.service.domain.repository.LikedVideoRepository

class InsertLikedVideoUseCase(private val repository: LikedVideoRepository) {
    suspend operator fun invoke(video: LikedVideo) {
        repository.insertLikedVideo(video)
    }
}
