package dev.alimmz.cinemood.feature.settings.presentation

import dev.alimmz.cinemood.core.domain.entity.ThemeMode
import dev.alimmz.cinemood.core.presentation.mvi.MviUiState

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val notificationsEnabled: Boolean = true,
) : MviUiState
