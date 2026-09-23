package dev.alimmz.cinemood.feature.settings.presentation

import dev.alimmz.cinemood.core.domain.entity.ThemeMode
import dev.alimmz.cinemood.core.presentation.mvi.MviUiAction

sealed interface SettingsUiAction : MviUiAction {
    data class ThemeModeSelected(val themeMode: ThemeMode) : SettingsUiAction
    data class NotificationsToggled(val enabled: Boolean) : SettingsUiAction
}
