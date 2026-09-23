package dev.alimmz.cinemood.feature.settings.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import dev.alimmz.cinemood.core.presentation.mvi.MviViewModel
import dev.alimmz.cinemood.core.domain.repository.ThemeRepository

class SettingsViewModel(
    private val themeRepository: ThemeRepository,
) : MviViewModel<SettingsUiState, SettingsUiAction>(
    initialState = SettingsUiState(),
) {
    init {
        themeRepository.themeMode
            .onEach { mode ->
                updateState { copy(themeMode = mode) }
            }
            .launchIn(viewModelScope)
    }

    override fun onAction(action: SettingsUiAction) {
        when (action) {
            is SettingsUiAction.ThemeModeSelected -> {
                viewModelScope.launch {
                    themeRepository.setThemeMode(action.themeMode)
                }
            }
            is SettingsUiAction.NotificationsToggled -> updateState { copy(notificationsEnabled = action.enabled) }
        }
    }
}
