package tech.nullexdev.cinemood.presentation.app

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tech.nullexdev.cinemood.core.navigation.Screen
import tech.nullexdev.cinemood.core.presentation.mvi.MviViewModel
import tech.nullexdev.cinemood.core.domain.repository.ThemeRepository

class AppViewModel(
    private val themeRepository: ThemeRepository,
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
