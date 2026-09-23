package dev.alimmz.cinemood.service.domain.di

import org.koin.dsl.module
import dev.alimmz.cinemood.service.domain.usecase.DeleteLikedVideoUseCase
import dev.alimmz.cinemood.service.domain.usecase.GetLikedVideoUseCase
import dev.alimmz.cinemood.service.domain.usecase.GetLikedVideosUseCase
import dev.alimmz.cinemood.service.domain.usecase.InsertLikedVideoUseCase

val useCaseModule = module {
    factory { GetLikedVideosUseCase(get()) }
    factory { InsertLikedVideoUseCase(get()) }
    factory { DeleteLikedVideoUseCase(get()) }
    factory { GetLikedVideoUseCase(get()) }
}
