package tech.nullexdev.cinemood

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.koin.compose.viewmodel.koinViewModel
import tech.nullexdev.cinemood.core.navigation.Screen
import tech.nullexdev.cinemood.core.presentation.components.CMNavigationBar
import tech.nullexdev.cinemood.core.presentation.components.CMNavigationRail
import tech.nullexdev.cinemood.core.presentation.components.SystemAppearance
import tech.nullexdev.cinemood.feature.favorite.FavoriteScreen
import tech.nullexdev.cinemood.feature.home.HomeScreen
import tech.nullexdev.cinemood.feature.home.MovieDetailScreen
import tech.nullexdev.cinemood.feature.search.SearchScreen
import tech.nullexdev.cinemood.feature.settings.SettingsScreen
import tech.nullexdev.cinemood.presentation.app.AppUiAction
import tech.nullexdev.cinemood.presentation.app.AppViewModel
import tech.nullexdev.cinemood.core.presentation.components.SharedMoviePosterDefaults
import tech.nullexdev.cinemood.navigation.movieDetailNavMetadata
import tech.nullexdev.cinemood.navigation.sharedNavSizeTransform
import tech.nullexdev.cinemood.theme.MyKMPAppTheme
import tech.nullexdev.cinemood.theme.ThemeState

val navSerializationConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Screen.Home::class)
            subclass(Screen.Search::class)
            subclass(Screen.Favorite::class)
            subclass(Screen.Settings::class)
            subclass(Screen.MovieDetail::class)
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
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isHorizontal = maxWidth > maxHeight

            Scaffold(
                bottomBar = {
                    if (!isHorizontal) {
                        CMNavigationBar(
                            currentScreen = uiState.currentScreen,
                            onNavigate = { screen: Screen ->
                                viewModel.onAction(AppUiAction.BottomNavSelected(screen))
                            },
                        )
                    }
                },
                contentWindowInsets = WindowInsets(0, 0, 0, 0)
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = innerPadding.calculateBottomPadding())
                ) {
                    if (isHorizontal) {
                        CMNavigationRail(
                            currentScreen = uiState.currentScreen,
                            onNavigate = { screen: Screen ->
                                viewModel.onAction(AppUiAction.BottomNavSelected(screen))
                            }
                        )
                    }

                    Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                        SharedTransitionLayout {
                            NavDisplay(
                                backStack = backStack,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                sizeTransform = sharedNavSizeTransform(),
                                onBack = {
                                    if (backStack.size > 1) {
                                        backStack.removeLast()
                                    }
                                },
                                entryProvider = entryProvider {
                                entry<Screen.Home> {
                                    val scope = LocalNavAnimatedContentScope.current
                                    if (scope.transition.targetState == EnterExitState.Visible) {
                                        SystemAppearance(
                                            isLight = !themeState.isDarkTheme(),
                                            statusBarColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
                                        )
                                    }
                                    HomeScreen(
                                        sharedTransitionScope = this@SharedTransitionLayout,
                                        animatedVisibilityScope = scope,
                                        onMovieClick = { movie ->
                                            backStack.add(
                                                Screen.MovieDetail(
                                                    movieId = movie.id,
                                                    movieTitle = movie.title,
                                                    moviePoster = movie.poster,
                                                    posterCornerRadiusDp = SharedMoviePosterDefaults.verticalCardCornerRadius.value.toInt(),
                                                )
                                            )
                                        }
                                    )
                                }
                                entry<Screen.Search> {
                                    val scope = LocalNavAnimatedContentScope.current
                                    if (scope.transition.targetState == EnterExitState.Visible) {
                                        SystemAppearance(
                                            isLight = !themeState.isDarkTheme(),
                                            statusBarColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
                                        )
                                    }
                                    SearchScreen(
                                        sharedTransitionScope = this@SharedTransitionLayout,
                                        animatedVisibilityScope = scope,
                                        onMovieClick = { movie ->
                                            backStack.add(
                                                Screen.MovieDetail(
                                                    movieId = movie.id,
                                                    movieTitle = movie.title,
                                                    moviePoster = movie.poster,
                                                    posterCornerRadiusDp = SharedMoviePosterDefaults.cardCornerRadius.value.toInt(),
                                                )
                                            )
                                        }
                                    )
                                }
                                entry<Screen.Favorite> {
                                    val scope = LocalNavAnimatedContentScope.current
                                    if (scope.transition.targetState == EnterExitState.Visible) {
                                        SystemAppearance(
                                            isLight = !themeState.isDarkTheme(),
                                            statusBarColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
                                        )
                                    }
                                    FavoriteScreen(
                                        sharedTransitionScope = this@SharedTransitionLayout,
                                        animatedVisibilityScope = scope,
                                        onMovieClick = { movie ->
                                            backStack.add(
                                                Screen.MovieDetail(
                                                    movieId = movie.id,
                                                    movieTitle = movie.title,
                                                    moviePoster = movie.poster,
                                                    posterCornerRadiusDp = SharedMoviePosterDefaults.cardCornerRadius.value.toInt(),
                                                )
                                            )
                                        }
                                    )
                                }
                                entry<Screen.Settings> {
                                    val scope = LocalNavAnimatedContentScope.current
                                    if (scope.transition.targetState == EnterExitState.Visible) {
                                        SystemAppearance(
                                            isLight = !themeState.isDarkTheme(),
                                            statusBarColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
                                        )
                                    }
                                    SettingsScreen()
                                }
                                entry<Screen.MovieDetail>(
                                    metadata = movieDetailNavMetadata(),
                                ) { key ->
                                    MovieDetailScreen(
                                        movieId = key.movieId,
                                        movieTitle = key.movieTitle,
                                        moviePoster = key.moviePoster,
                                        posterCornerRadiusDp = key.posterCornerRadiusDp,
                                        sharedTransitionScope = this@SharedTransitionLayout,
                                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                        onBack = { backStack.removeLast() }
                                    )
                                }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    // App() // Koin might not be initialized in preview
}
