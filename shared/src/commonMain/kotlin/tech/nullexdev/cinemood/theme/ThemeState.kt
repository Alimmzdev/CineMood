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
import androidx.compose.ui.graphics.Color

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
val LocalThemeState = compositionLocalOf { ThemeState() }

// Updated app theme
@Composable
fun MyKMPAppTheme(
    themeState: ThemeState = remember { ThemeState() },
    content: @Composable () -> Unit
) {
    val darkTheme = themeState.isDarkTheme()
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = Color(0xFFFF0000),
            onPrimary = Color(0xFF690005),
            primaryContainer = Color(0xFF93000A),
            onPrimaryContainer = Color(0xFFFFDAD6),
            secondary = Color(0xFFE7BDB8),
            onSecondary = Color(0xFF442926),
            secondaryContainer = Color(0xFF5D3F3B),
            onSecondaryContainer = Color(0xFFFFDAD6),
            tertiary = Color(0xFFE2C27D),
            onTertiary = Color(0xFF402D04),
            tertiaryContainer = Color(0xFF594319),
            onTertiaryContainer = Color(0xFFFFDEA8),
            error = Color(0xFFFFB4AB),
            onError = Color(0xFF690005),
            errorContainer = Color(0xFF93000A),
            onErrorContainer = Color(0xFFFFDAD6),
            background = Color(0xFF1C1C1C),
            onBackground = Color(0xFFEDE0DE),
            surface = Color(0xFF1C1C1C),
            onSurface = Color(0xFFEDE0DE),
            surfaceVariant = Color(0xFF2C2C2C),
            onSurfaceVariant = Color(0xFFD8C2BE),
            outline = Color(0xFFA08C89),
            outlineVariant = Color(0xFF534341),
            scrim = Color(0xFF000000),
            inverseSurface = Color(0xFFEDE0DE),
            inverseOnSurface = Color(0xFF362F2E),
            inversePrimary = Color(0xFFFF0000),
            surfaceTint = Color(0xFFFF0000),
        )
    } else {
        lightColorScheme(
            primary = Color(0xFFFF0000),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFFFDAD6),
            onPrimaryContainer = Color(0xFF410002),
            secondary = Color(0xFF775652),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFFFDAD6),
            onSecondaryContainer = Color(0xFF2C1512),
            tertiary = Color(0xFF755B2E),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFFFDEA8),
            onTertiaryContainer = Color(0xFF271900),
            error = Color(0xFFBA1A1A),
            onError = Color(0xFFFFFFFF),
            errorContainer = Color(0xFFFFDAD6),
            onErrorContainer = Color(0xFF410002),
            background = Color(0xFFFFFBFF),
            onBackground = Color(0xFF201A19),
            surface = Color(0xFFFFFBFF),
            onSurface = Color(0xFF201A19),
            surfaceVariant = Color(0xFFF5DDDA),
            onSurfaceVariant = Color(0xFF534341),
            outline = Color(0xFF857370),
            outlineVariant = Color(0xFFD8C2BE),
            scrim = Color(0xFF000000),
            inverseSurface = Color(0xFF362F2E),
            inverseOnSurface = Color(0xFFFBEEEC),
            inversePrimary = Color(0xFFFFB4AB),
            surfaceTint = Color(0xFFFF0000),
        )
    }

    CompositionLocalProvider(LocalThemeState provides themeState) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography(),
            content = content
        )
    }
}
