package tech.nullexdev.cinemood.service.domain.usecase

import kotlinx.coroutines.flow.Flow
import tech.nullexdev.cinemood.service.domain.entity.LikedVideo
import tech.nullexdev.cinemood.service.domain.repository.LikedVideoRepository

class GetLikedVideosUseCase(private val repository: LikedVideoRepository) {
    operator fun invoke(): Flow<List<LikedVideo>> {
        return repository.getLikedVideos()
    }
}
