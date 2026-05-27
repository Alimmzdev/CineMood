package tech.nullexdev.cinemood

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import tech.nullexdev.cinemood.di.initKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        _root_ide_package_.tech.nullexdev.cinemood.di.initKoin()
        _root_ide_package_.tech.nullexdev.cinemood.App()
    }
}