package dev.alimmz.cinemood.presentation.app

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import dev.alimmz.cinemood.core.navigation.Screen
import dev.alimmz.cinemood.core.presentation.mvi.MviViewModel
import dev.alimmz.cinemood.core.domain.repository.ThemeRepository

class AppViewModel(
    themeRepository: ThemeRepository,
) : MviViewModel<AppUiState, AppUiAction>(
    initialState = AppUiState(),
) {
    init {
        themeRepository.themeMode
            .onEach { mode ->
                updateState { copy(themeMode = mode) }
            }
            .launchIn(viewModelScope)
    }

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
