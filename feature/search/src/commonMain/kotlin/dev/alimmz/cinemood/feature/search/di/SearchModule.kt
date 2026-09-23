package dev.alimmz.cinemood.feature.search.di

import dev.alimmz.cinemood.feature.search.presentation.SearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val searchModule = module {
    viewModelOf(::SearchViewModel)
}
