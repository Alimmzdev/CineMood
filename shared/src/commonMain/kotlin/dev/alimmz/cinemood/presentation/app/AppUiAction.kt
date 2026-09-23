package dev.alimmz.cinemood.presentation.app

import dev.alimmz.cinemood.core.domain.entity.ThemeMode
import dev.alimmz.cinemood.core.presentation.mvi.MviUiAction
import dev.alimmz.cinemood.core.navigation.Screen

sealed interface AppUiAction : MviUiAction {
    data class BottomNavSelected(val screen: Screen) : AppUiAction
    data class ThemeModeChanged(val themeMode: ThemeMode) : AppUiAction
}
