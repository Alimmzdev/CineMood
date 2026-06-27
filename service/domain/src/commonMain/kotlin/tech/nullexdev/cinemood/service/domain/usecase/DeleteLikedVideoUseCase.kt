package tech.nullexdev.cinemood.service.domain.usecase

import tech.nullexdev.cinemood.service.domain.repository.LikedVideoRepository

class DeleteLikedVideoUseCase(private val repository: LikedVideoRepository) {
    suspend operator fun invoke(tmdbId: Int) {
        repository.deleteLikedVideo(tmdbId)
    }
}
