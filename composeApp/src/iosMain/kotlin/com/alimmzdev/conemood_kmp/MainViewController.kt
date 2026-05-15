package com.alimmzdev.conemood_kmp

import androidx.compose.ui.window.ComposeUIViewController
import com.alimmzdev.conemood_kmp.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}