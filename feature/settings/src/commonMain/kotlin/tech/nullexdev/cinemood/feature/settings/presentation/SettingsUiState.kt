package tech.nullexdev.cinemood.feature.settings.presentation

import tech.nullexdev.cinemood.core.domain.entity.ThemeMode
import tech.nullexdev.cinemood.core.presentation.mvi.MviUiState

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val notificationsEnabled: Boolean = true,
) : MviUiState
