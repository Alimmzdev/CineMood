package com.alimmzdev.conemood_kmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.alimmzdev.conemood_kmp.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "CineMookKmp",
    ) {
        App()
    }
}