package dev.alimmz.cinemood.service.domain.usecase

import kotlinx.coroutines.flow.Flow
import dev.alimmz.cinemood.service.domain.entity.LikedVideo
import dev.alimmz.cinemood.service.domain.repository.LikedVideoRepository

class GetLikedVideoUseCase(private val repository: LikedVideoRepository) {
    operator fun invoke(tmdbId: Int): Flow<LikedVideo?> {
        return repository.getLikedVideo(tmdbId)
    }
}
