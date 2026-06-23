package tech.nullexdev.cinemood.util

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import tech.nullexdev.cinemood.feature.favorite.presentation.FavoriteViewModel
import tech.nullexdev.cinemood.feature.home.presentation.HomeViewModel
import tech.nullexdev.cinemood.feature.home.presentation.MovieDetailViewModel
import tech.nullexdev.cinemood.feature.search.presentation.SearchViewModel
import tech.nullexdev.cinemood.feature.settings.presentation.SettingsViewModel
import tech.nullexdev.cinemood.presentation.app.AppViewModel

object KoinHelper : KoinComponent {
    val shared = this
    fun getAppViewModel(): AppViewModel = get()
    fun getHomeViewModel(): HomeViewModel = get()
    fun getSearchViewModel(): SearchViewModel = get()
    fun getFavoriteViewModel(): FavoriteViewModel = get()
    fun getSettingsViewModel(): SettingsViewModel = get()

    /**
     * MovieDetailViewModel takes the movie id as a constructor parameter
     * (see `homeModule`), so it must be resolved with [parametersOf].
     */
    fun getMovieDetailViewModel(movieId: Int): MovieDetailViewModel =
        get { parametersOf(movieId) }
}
