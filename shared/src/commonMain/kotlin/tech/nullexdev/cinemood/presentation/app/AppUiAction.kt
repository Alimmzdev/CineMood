package tech.nullexdev.cinemood.presentation.app

import tech.nullexdev.cinemood.core.domain.presentation.mvi.MviUiAction
import tech.nullexdev.cinemood.core.navigation.Screen
import tech.nullexdev.cinemood.theme.ThemeState

sealed interface AppUiAction : MviUiAction {
    data class BottomNavSelected(val screen: Screen) : AppUiAction
    data class ThemeModeChanged(val themeMode: ThemeState.ThemeMode) : AppUiAction
}
