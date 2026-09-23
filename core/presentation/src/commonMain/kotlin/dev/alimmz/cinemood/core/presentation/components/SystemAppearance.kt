package dev.alimmz.cinemood.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Changes the system bars (status bar and navigation bar) appearance.
 * 
 * @param isLight If true, the system bars will be configured for a light background (dark icons).
 *                If false, the system bars will be configured for a dark background (light icons).
 * @param statusBarColor The background color of the status bar. Defaults to transparent.
 * @param navigationBarColor The background color of the navigation bar. Defaults to transparent.
 */
@Composable
expect fun SystemAppearance(
    isLight: Boolean,
    statusBarColor: Color = Color.Transparent,
    navigationBarColor: Color = Color.Transparent
)
