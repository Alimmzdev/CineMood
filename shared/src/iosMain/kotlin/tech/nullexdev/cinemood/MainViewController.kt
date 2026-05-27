package tech.nullexdev.cinemood

import androidx.compose.ui.window.ComposeUIViewController
import tech.nullexdev.cinemood.di.initKoin

fun MainViewController() = ComposeUIViewController {
    _root_ide_package_.tech.nullexdev.cinemood.di.initKoin()
    _root_ide_package_.tech.nullexdev.cinemood.App()
}