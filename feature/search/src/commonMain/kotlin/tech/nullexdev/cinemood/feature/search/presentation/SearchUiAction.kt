package tech.nullexdev.cinemood.feature.search.presentation

import tech.nullexdev.cinemood.core.presentation.mvi.MviUiAction

sealed interface SearchUiAction : MviUiAction {
    data class QueryChanged(val query: String) : SearchUiAction
    data object SearchSubmitted : SearchUiAction
    data object ClearQuery : SearchUiAction
}
