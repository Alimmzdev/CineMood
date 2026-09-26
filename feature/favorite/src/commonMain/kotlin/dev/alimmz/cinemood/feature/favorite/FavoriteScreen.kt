package dev.alimmz.cinemood.feature.favorite

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.viewmodel.koinViewModel
import coil3.compose.SubcomposeAsyncImage
import dev.alimmz.cinemood.core.presentation.components.ErrorState
import dev.alimmz.cinemood.core.presentation.components.moviePosterKey
import dev.alimmz.cinemood.core.presentation.components.rememberAnimatedPosterCornerRadius
import dev.alimmz.cinemood.core.presentation.components.sharedMoviePosterModifier
import dev.alimmz.cinemood.feature.favorite.presentation.FavoriteUiAction
import dev.alimmz.cinemood.feature.favorite.presentation.model.FavoriteMovieItem
import dev.alimmz.cinemood.feature.favorite.presentation.FavoriteViewModel
import dev.alimmz.cinemood.service.domain.model.Movie

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = koinViewModel(),
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    onMovieClick: (Movie) -> Unit = {},
    onNavigateToDiscover: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var pendingRemovalId by rememberSaveable { mutableStateOf<Int?>(null) }
    val pendingRemoval = uiState.favorites.firstOrNull { it.id == pendingRemovalId }

    if (pendingRemoval != null) {
        AlertDialog(
            onDismissRequest = { pendingRemovalId = null },
            title = { Text("Remove from favorites?") },
            text = { Text("Remove “${pendingRemoval.title}” from your saved movies?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onAction(FavoriteUiAction.RemoveFavorite(pendingRemoval))
                    pendingRemovalId = null
                }) { Text("Remove", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { pendingRemovalId = null }) { Text("Cancel") }
            },
        )
    }

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
                        if (uiState.favorites.isNotEmpty()) {
                            Text(
                                "${uiState.favorites.size} ${if (uiState.favorites.size == 1) "movie" else "movies"} saved",
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
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            when {
                uiState.isLoading && uiState.favorites.isEmpty() -> {
                    CircularProgressIndicator()
                }
                uiState.errorMessage != null && uiState.favorites.isEmpty() -> {
                    ErrorState(
                        message = uiState.errorMessage ?: "Unable to load favorites",
                        onRetry = { viewModel.onAction(FavoriteUiAction.Refresh) },
                    )
                }
                uiState.favorites.isEmpty() -> {
                    EmptyFavoritesState(onNavigateToDiscover = onNavigateToDiscover)
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        if (uiState.errorMessage != null) {
                            item(key = "error") {
                                ErrorState(
                                    message = uiState.errorMessage ?: "Unable to update favorites",
                                    onRetry = { viewModel.onAction(FavoriteUiAction.Refresh) },
                                )
                            }
                        }
                        itemsIndexed(uiState.favorites, key = { _, movie -> movie.id }) { index, favorite ->
                            FavoriteRow(
                                favorite = favorite,
                                index = index + 1,
                                modifier = Modifier.animateItem(),
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope,
                                onClick = {
                                    onMovieClick(Movie(
                                        id = favorite.id,
                                        title = favorite.title,
                                        poster = favorite.poster,
                                        genres = favorite.genres,
                                        images = persistentListOf(),
                                    ))
                                },
                                onRemove = { pendingRemovalId = favorite.id },
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun FavoriteRow(
    favorite: FavoriteMovieItem,
    index: Int,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope?,
    animatedVisibilityScope: AnimatedVisibilityScope?,
    onClick: () -> Unit,
    onRemove: () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Row(
            modifier = Modifier.clickable(onClick = onClick).padding(start = 12.dp, end = 4.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = index.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.widthIn(min = 20.dp),
            )
            val posterModifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
                val cornerRadius = rememberAnimatedPosterCornerRadius(
                    animatedVisibilityScope = animatedVisibilityScope,
                    listCornerRadius = 10.dp,
                    isDetailDestination = false,
                )
                with(sharedTransitionScope) {
                    sharedMoviePosterModifier(
                        posterKey = moviePosterKey(favorite.id),
                        animatedVisibilityScope = animatedVisibilityScope,
                        cornerRadius = cornerRadius,
                    )
                }
            } else {
                Modifier.clip(RoundedCornerShape(10.dp))
            }
            SubcomposeAsyncImage(
                model = favorite.poster,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = posterModifier.size(width = 56.dp, height = 84.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                loading = { PosterPlaceholder() },
                error = { PosterPlaceholder() },
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = favorite.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                if (favorite.genres.isNotEmpty()) {
                    Text(
                        text = favorite.genres.joinToString(" · "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(12.dp))
                        Text("Saved", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Remove ${favorite.title} from favorites",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun PosterPlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Icon(
            Icons.Outlined.Movie,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp),
        )
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
