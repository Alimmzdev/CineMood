package tech.nullexdev.cinemood

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import tech.nullexdev.cinemood.di.initKoin
import java.awt.Window

fun main(): kotlin.Unit = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "CineMookKmp",
    ) {
        App()
    }
}