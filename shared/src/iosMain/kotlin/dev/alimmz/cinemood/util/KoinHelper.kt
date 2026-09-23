package dev.alimmz.cinemood.util

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import dev.alimmz.cinemood.feature.favorite.presentation.FavoriteViewModel
import dev.alimmz.cinemood.feature.home.presentation.HomeViewModel
import dev.alimmz.cinemood.feature.home.presentation.MovieDetailViewModel
import dev.alimmz.cinemood.feature.search.presentation.SearchViewModel
import dev.alimmz.cinemood.feature.settings.presentation.SettingsViewModel
import dev.alimmz.cinemood.presentation.app.AppViewModel

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
