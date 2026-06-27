package tech.nullexdev.cinemood.feature.favorite

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.viewmodel.koinViewModel
import tech.nullexdev.cinemood.core.presentation.components.MovieCard
import tech.nullexdev.cinemood.feature.favorite.presentation.viewmodel.FavoriteViewModel
import tech.nullexdev.cinemood.service.domain.model.Movie

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = koinViewModel(),
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    onMovieClick: (Movie) -> Unit = {},
    onNavigateToDiscover: () -> Unit = {},
) {
    val likedVideos by viewModel.likedVideos.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(
                            "Favorites",
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-1).sp,
                        )
                        if (likedVideos.isNotEmpty()) {
                            Text(
                                "${likedVideos.size} items saved",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                )
        ) {
            when {
                likedVideos.isEmpty() -> {
                    EmptyFavoritesState(onNavigateToDiscover = onNavigateToDiscover)
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp, bottom = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        likedVideos.chunked(2).forEach { rowMovies ->
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    rowMovies.forEach { favorite ->
                                        Box(modifier = Modifier.weight(1f)) {
                                            val movie = Movie(
                                                id = favorite.tmdbId,
                                                title = favorite.title,
                                                poster = favorite.posterUrl,
                                                genres = persistentListOf(),
                                                images = persistentListOf()
                                            )
                                            MovieCard(
                                                movie = movie,
                                                sharedTransitionScope = sharedTransitionScope,
                                                animatedVisibilityScope = animatedVisibilityScope,
                                                onClick = { onMovieClick(movie) }
                                            )
                                        }
                                    }
                                    if (rowMovies.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyFavoritesState(
    onNavigateToDiscover: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(120.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHigh
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.HeartBroken,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            "Your list is empty",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Start adding movies to your favorites to see them here.",
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = onNavigateToDiscover,
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Discover Movies")
        }
    }
}
