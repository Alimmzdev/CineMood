package tech.nullexdev.cinemood.presentation.app

import tech.nullexdev.cinemood.core.domain.entity.ThemeMode
import tech.nullexdev.cinemood.core.presentation.mvi.MviUiState
import tech.nullexdev.cinemood.core.navigation.Screen

data class AppUiState(
    val currentScreen: Screen = Screen.Home,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
) : MviUiState
