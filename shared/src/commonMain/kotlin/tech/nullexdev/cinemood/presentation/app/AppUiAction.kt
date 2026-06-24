package tech.nullexdev.cinemood.presentation.app

import tech.nullexdev.cinemood.core.domain.entity.ThemeMode
import tech.nullexdev.cinemood.core.presentation.mvi.MviUiAction
import tech.nullexdev.cinemood.core.navigation.Screen

sealed interface AppUiAction : MviUiAction {
    data class BottomNavSelected(val screen: Screen) : AppUiAction
    data class ThemeModeChanged(val themeMode: ThemeMode) : AppUiAction
}
