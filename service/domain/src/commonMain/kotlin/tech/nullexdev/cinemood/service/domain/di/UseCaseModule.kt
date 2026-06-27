package tech.nullexdev.cinemood.service.domain.di

import org.koin.dsl.module
import tech.nullexdev.cinemood.service.domain.usecase.DeleteLikedVideoUseCase
import tech.nullexdev.cinemood.service.domain.usecase.GetLikedVideoUseCase
import tech.nullexdev.cinemood.service.domain.usecase.GetLikedVideosUseCase
import tech.nullexdev.cinemood.service.domain.usecase.InsertLikedVideoUseCase

val useCaseModule = module {
    factory { GetLikedVideosUseCase(get()) }
    factory { InsertLikedVideoUseCase(get()) }
    factory { DeleteLikedVideoUseCase(get()) }
    factory { GetLikedVideoUseCase(get()) }
}
