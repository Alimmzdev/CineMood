package tech.nullexdev.cinemood.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

class ThemeState {
    enum class ThemeMode {
        LIGHT, DARK, SYSTEM
    }

    private val _themeMode = mutableStateOf(ThemeMode.SYSTEM)
    val themeMode: MutableState<ThemeMode> = _themeMode

//    fun setThemeMode(mode: ThemeMode) {
//        _themeMode.value = mode
//    }

    @Composable
    fun isDarkTheme(): Boolean {
        return when (themeMode.value) {
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
        }
    }
}

// Create a composition local for the theme state
val LocalThemeState = compositionLocalOf { _root_ide_package_.tech.nullexdev.cinemood.theme.ThemeState() }

// Updated app theme
@Composable
fun MyKMPAppTheme(
    themeState: tech.nullexdev.cinemood.theme.ThemeState = remember { _root_ide_package_.tech.nullexdev.cinemood.theme.ThemeState() },
    content: @Composable () -> Unit
) {
    val darkTheme = themeState.isDarkTheme()
    val colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()

    CompositionLocalProvider(_root_ide_package_.tech.nullexdev.cinemood.theme.LocalThemeState provides themeState) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography(),
            content = content
        )
    }
}