package tech.nullexdev.cinemood.feature.settings.presentation

import tech.nullexdev.cinemood.core.domain.entity.ThemeMode
import tech.nullexdev.cinemood.core.presentation.mvi.MviUiAction

sealed interface SettingsUiAction : MviUiAction {
    data class ThemeModeSelected(val themeMode: ThemeMode) : SettingsUiAction
    data class NotificationsToggled(val enabled: Boolean) : SettingsUiAction
}
