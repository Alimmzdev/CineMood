package dev.alimmz.cinemood.presentation.app

import dev.alimmz.cinemood.core.domain.entity.ThemeMode
import dev.alimmz.cinemood.core.presentation.mvi.MviUiState
import dev.alimmz.cinemood.core.navigation.Screen

data class AppUiState(
    val currentScreen: Screen = Screen.Home,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
) : MviUiState
