package dev.alimmz.cinemood

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.alimmz.cinemood.di.initKoin

fun main(): Unit = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "CineMood",
    ) {
        App()
    }
}
