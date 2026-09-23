package dev.alimmz.cinemood.service.domain.usecase

import kotlinx.coroutines.flow.Flow
import dev.alimmz.cinemood.service.domain.entity.LikedVideo
import dev.alimmz.cinemood.service.domain.repository.LikedVideoRepository

class GetLikedVideosUseCase(private val repository: LikedVideoRepository) {
    operator fun invoke(): Flow<List<LikedVideo>> {
        return repository.getLikedVideos()
    }
}
