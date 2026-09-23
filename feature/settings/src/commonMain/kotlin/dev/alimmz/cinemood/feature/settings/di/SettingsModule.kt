package dev.alimmz.cinemood.feature.settings.di

import dev.alimmz.cinemood.feature.settings.presentation.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsModule = module {
    viewModelOf(::SettingsViewModel)
}
