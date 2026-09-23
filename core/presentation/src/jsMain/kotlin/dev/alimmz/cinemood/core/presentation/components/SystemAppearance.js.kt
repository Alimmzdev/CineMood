package dev.alimmz.cinemood.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
actual fun SystemAppearance(
    isLight: Boolean,
    statusBarColor: Color,
    navigationBarColor: Color
) {
    // No-op for JS
}
