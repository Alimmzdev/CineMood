package tech.nullexdev.cinemood

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import tech.nullexdev.cinemood.di.initKoin

fun main(): Unit = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "CineMood",
    ) {
        App()
    }
}