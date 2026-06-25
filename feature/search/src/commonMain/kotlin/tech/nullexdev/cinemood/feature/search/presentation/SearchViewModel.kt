package tech.nullexdev.cinemood.feature.search.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import tech.nullexdev.cinemood.core.domain.common.BaseResult
import tech.nullexdev.cinemood.core.presentation.mvi.MviViewModel
import tech.nullexdev.cinemood.service.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class SearchViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase,
) : MviViewModel<SearchUiState, SearchUiAction>(
    initialState = SearchUiState(),
) {
    private var searchJob: Job? = null

    override fun onAction(action: SearchUiAction) {
        when (action) {
            is SearchUiAction.QueryChanged -> {
                updateState { copy(query = action.query, errorMessage = null) }
                searchJob?.cancel()
                if (action.query.trim().isNotEmpty()) {
                    searchJob = viewModelScope.launch {
                        delay(500.milliseconds)
                        searchMovies()
                    }
                } else {
                    updateState { copy(movies = persistentListOf(), hasSearched = false) }
                }
            }
            SearchUiAction.SearchSubmitted -> {
                searchJob?.cancel()
                searchMovies()
            }
            SearchUiAction.ClearQuery -> {
                searchJob?.cancel()
                updateState {
                    copy(
                        query = "",
                        movies = persistentListOf(),
                        errorMessage = null,
                        hasSearched = false,
                        isLoading = false,
                    )
                }
            }
        }
    }
    private fun searchMovies() {
        val query: String = currentState.query.trim()
        if (query.isEmpty()) {
            updateState { copy(errorMessage = "Enter a search query") }
            return
        }
        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            searchMoviesUseCase(SearchMoviesUseCase.Params(query = query)).collect { result ->
                when (result) {
                    is BaseResult.Success -> updateState {
                        copy(
                            isLoading = false,
                            movies = result.data.movies,
                            hasSearched = true,
                            errorMessage = null,
                        )
                    }
                    is BaseResult.Error -> updateState {
                        copy(
                            isLoading = false,
                            hasSearched = true,
                            errorMessage = result.message ?: result.exception.message,
                        )
                    }
                }
            }
        }
    }
}
