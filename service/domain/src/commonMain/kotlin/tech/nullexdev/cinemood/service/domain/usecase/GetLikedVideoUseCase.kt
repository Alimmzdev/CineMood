package tech.nullexdev.cinemood.service.domain.usecase

import kotlinx.coroutines.flow.Flow
import tech.nullexdev.cinemood.service.domain.entity.LikedVideo
import tech.nullexdev.cinemood.service.domain.repository.LikedVideoRepository

class GetLikedVideoUseCase(private val repository: LikedVideoRepository) {
    operator fun invoke(tmdbId: Int): Flow<LikedVideo?> {
        return repository.getLikedVideo(tmdbId)
    }
}
