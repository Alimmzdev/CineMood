package dev.alimmz.cinemood.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import dev.alimmz.cinemood.core.domain.entity.ThemeMode

class ThemeState {
    private val _themeMode = mutableStateOf(ThemeMode.SYSTEM)
    val themeMode: MutableState<ThemeMode> = _themeMode

    @Composable
    fun isDarkTheme(): Boolean {
        return when (themeMode.value) {
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
        }
    }
}

val LocalThemeState = compositionLocalOf { ThemeState() }