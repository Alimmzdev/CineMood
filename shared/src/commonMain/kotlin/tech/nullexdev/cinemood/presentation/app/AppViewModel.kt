package tech.nullexdev.cinemood.presentation.app

import tech.nullexdev.cinemood.core.navigation.Screen
import tech.nullexdev.cinemood.core.domain.presentation.mvi.MviViewModel

class AppViewModel : MviViewModel<AppUiState, AppUiAction>(
    initialState = AppUiState(),
) {
    override fun onAction(action: AppUiAction) {
        when (action) {
            is AppUiAction.BottomNavSelected -> navigateTo(action.screen)
            is AppUiAction.ThemeModeChanged -> updateState { copy(themeMode = action.themeMode) }
        }
    }
    private fun navigateTo(screen: Screen) {
        if (currentState.currentScreen == screen) {
            return
        }
        updateState { copy(currentScreen = screen) }
    }
}
