package tech.nullexdev.cinemood.core.presentation.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class MviViewModel<UiState : MviUiState, UiAction : MviUiAction>(
    initialState: UiState,
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(initialState)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    protected val currentState: UiState
        get() = _uiState.value
    protected fun updateState(reducer: UiState.() -> UiState) {
        _uiState.update(reducer)
    }
    abstract fun onAction(action: UiAction)
}
