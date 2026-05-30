package tech.nullexdev.cinemood.feature.home.presentation

import androidx.lifecycle.viewModelScope
import tech.nullexdev.cinemood.core.domain.common.BaseResult
import tech.nullexdev.cinemood.core.domain.presentation.mvi.MviViewModel
import tech.nullexdev.cinemood.service.domain.usecase.GetMoviesUseCase
import tech.nullexdev.cinemood.core.navigation.Screen
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
) : MviViewModel<HomeUiState, HomeUiAction>(
    initialState = HomeUiState(),
) {
    init {
        onAction(HomeUiAction.LoadMovies)
    }

    override fun onAction(action: HomeUiAction) {
        when (action) {
            HomeUiAction.LoadMovies -> loadMovies(page = 1, replaceMovies = true)
            HomeUiAction.Refresh -> loadMovies(page = 1, replaceMovies = true)
            HomeUiAction.LoadNextPage -> {
                if (currentState.hasNextPage && !currentState.isLoading) {
                    loadMovies(page = currentState.currentPage + 1, replaceMovies = false)
                }
            }
            HomeUiAction.SearchClicked -> navigateTo(Screen.Search)
            HomeUiAction.FavoriteClicked -> navigateTo(Screen.Favorite)
        }
    }

    private fun loadMovies(page: Int, replaceMovies: Boolean) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            getMoviesUseCase(GetMoviesUseCase.Params(page = page)).collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        val pageData = result.data
                        updateState {
                            copy(
                                isLoading = false,
                                movies = if (replaceMovies) pageData.movies else movies + pageData.movies,
                                currentPage = pageData.currentPage,
                                hasNextPage = pageData.hasNextPage,
                                errorMessage = null,
                            )
                        }
                    }

                    is BaseResult.Error -> updateState {
                        copy(
                            isLoading = false,
                            errorMessage = result.message ?: result.exception.message,
                        )
                    }
                }
            }
        }
    }

    private fun navigateTo(screen: Screen) {
        // Navigation should be handled by AppViewModel or a dedicated Navigator.
        // For now, this is a placeholder to keep the code compiling if side effects are not yet implemented.
        // In a real app, you might update a 'navigation' state or emit a side effect.
    }
}
