package tech.nullexdev.cinemood.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
actual fun SystemAppearance(
    isLight: Boolean,
    statusBarColor: Color,
    navigationBarColor: Color
) {
    // No-op for iOS as we are using native SwiftUI
}
