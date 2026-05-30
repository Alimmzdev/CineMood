package tech.nullexdev.cinemood

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.koin.compose.viewmodel.koinViewModel
import tech.nullexdev.cinemood.core.presentation.components.CMNavigationBar
import tech.nullexdev.cinemood.core.presentation.components.CMTopAppBar
import tech.nullexdev.cinemood.core.navigation.Screen
import tech.nullexdev.cinemood.feature.favorite.FavoriteScreen
import tech.nullexdev.cinemood.feature.home.HomeScreen
import tech.nullexdev.cinemood.feature.search.SearchScreen
import tech.nullexdev.cinemood.feature.settings.SettingsScreen
import tech.nullexdev.cinemood.presentation.app.AppUiAction
import tech.nullexdev.cinemood.presentation.app.AppViewModel
import tech.nullexdev.cinemood.theme.MyKMPAppTheme
import tech.nullexdev.cinemood.theme.ThemeState

val navSerializationConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Screen.Home::class)
            subclass(Screen.Search::class)
            subclass(Screen.Favorite::class)
            subclass(Screen.Settings::class)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun App(
    viewModel: AppViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val themeState = remember { ThemeState() }
    val backStack = rememberNavBackStack(
        configuration = navSerializationConfig,
        uiState.currentScreen,
    )
    LaunchedEffect(uiState.themeMode) {
        themeState.themeMode.value = uiState.themeMode
    }
    LaunchedEffect(uiState.currentScreen) {
        if (backStack.last() != uiState.currentScreen) {
            backStack.clear()
            backStack.add(uiState.currentScreen)
        }
    }
    MyKMPAppTheme(themeState = themeState) {
        Scaffold(
            bottomBar = {
                CMNavigationBar(
                    currentScreen = uiState.currentScreen,
                    onNavigate = { screen ->
                        viewModel.onAction(AppUiAction.BottomNavSelected(screen))
                    },
                )
            },
        ) { innerPadding ->
            val bottomPadding = innerPadding.calculateBottomPadding()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = bottomPadding)
            ) {
                NavDisplay(
                    backStack = backStack,
                    onBack = {
                        if (backStack.size > 1) {
                            backStack.removeLast()
                        }
                    },
                    entryProvider = entryProvider {
                        entry<Screen.Home> { HomeScreen() }
                        entry<Screen.Search> { SearchScreen() }
                        entry<Screen.Favorite> { FavoriteScreen() }
                        entry<Screen.Settings> { SettingsScreen() }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    App(viewModel = AppViewModel())
}
