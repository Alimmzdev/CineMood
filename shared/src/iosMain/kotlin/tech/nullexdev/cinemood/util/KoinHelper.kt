package tech.nullexdev.cinemood.util

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import tech.nullexdev.cinemood.feature.home.presentation.HomeViewModel
import tech.nullexdev.cinemood.presentation.app.AppViewModel

object KoinHelper : KoinComponent {
    val shared = this
    fun getAppViewModel(): AppViewModel = get()
    fun getHomeViewModel(): HomeViewModel = get()
}
