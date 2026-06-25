package tech.nullexdev.cinemood.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import tech.nullexdev.cinemood.core.presentation.components.SharedTransitionAnimations
import tech.nullexdev.cinemood.core.presentation.components.SystemAppearance
import tech.nullexdev.cinemood.core.presentation.components.moviePosterKey
import tech.nullexdev.cinemood.core.presentation.components.rememberAnimatedPosterCornerRadius
import tech.nullexdev.cinemood.core.presentation.components.sharedDetailContentEnterTransition
import tech.nullexdev.cinemood.core.presentation.components.sharedDetailContentExitTransition
import tech.nullexdev.cinemood.core.presentation.components.sharedDetailOverlayEnterTransition
import tech.nullexdev.cinemood.core.presentation.components.sharedDetailPlayButtonEnterTransition
import tech.nullexdev.cinemood.core.presentation.components.sharedDetailPlayButtonExitTransition
import tech.nullexdev.cinemood.core.presentation.components.sharedDetailSectionContentEnterTransition
import tech.nullexdev.cinemood.core.presentation.components.sharedMoviePosterModifier
import tech.nullexdev.cinemood.core.presentation.components.shouldRenderSharedOverlay
import tech.nullexdev.cinemood.core.presentation.theme.LocalThemeState
import tech.nullexdev.cinemood.feature.home.presentation.MovieDetailUiAction
import tech.nullexdev.cinemood.feature.home.presentation.MovieDetailUiState
import tech.nullexdev.cinemood.feature.home.presentation.MovieDetailViewModel
import tech.nullexdev.cinemood.service.domain.model.MovieDetail

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun MovieDetailScreen(
    movieId: Int,
    movieTitle: String,
    moviePoster: String,
    posterCornerRadiusDp: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBack: () -> Unit,
    viewModel: MovieDetailViewModel = koinViewModel(key = "movie_detail_$movieId") {
        parametersOf(movieId)
    }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = remember(movieId) { ScrollState(0) }
    val themeState = LocalThemeState.current
    val darkTheme = themeState.isDarkTheme()
    val movieDetail: MovieDetail? = uiState.movieDetail
    val displayTitle: String = movieDetail?.title ?: movieTitle
    val displayPoster: String = movieDetail?.poster?.takeIf { it.isNotBlank() } ?: moviePoster
    val isSystemBarsLight = if (scrollState.value > 1000) !darkTheme else false
    SystemAppearance(isLight = isSystemBarsLight)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .verticalScroll(scrollState),
        ) {
            MovieDetailHeroSection(
                movieId = movieId,
                movieTitle = displayTitle,
                moviePoster = displayPoster,
                posterCornerRadiusDp = posterCornerRadiusDp,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                onBack = onBack,
            )
            MovieDetailFadeSection(
                uiState = uiState,
                movieDetail = movieDetail,
                animatedVisibilityScope = animatedVisibilityScope,
                onRetry = { viewModel.onAction(MovieDetailUiAction.Retry) },
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun MovieDetailFadeSection(
    uiState: MovieDetailUiState,
    movieDetail: MovieDetail?,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onRetry: () -> Unit,
) {
    val sectionShape = RoundedCornerShape(
        topStart = SharedTransitionAnimations.DETAIL_SECTION_TOP_RADIUS_DP.dp,
        topEnd = SharedTransitionAnimations.DETAIL_SECTION_TOP_RADIUS_DP.dp,
    )
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-20).dp)
            .then(
                with(animatedVisibilityScope) {
                    Modifier.animateEnterExit(
                        enter = sharedDetailContentEnterTransition(),
                        exit = sharedDetailContentExitTransition(),
                    )
                },
            ),
        shape = sectionShape,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 6.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 320,
                        easing = SharedTransitionAnimations.EASING,
                    ),
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AnimatedVisibility(
                visible = uiState.isLoading && movieDetail == null,
                enter = fadeIn(tween(280)),
                exit = fadeOut(tween(180)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
            AnimatedVisibility(
                visible = uiState.errorMessage != null && movieDetail == null,
                enter = sharedDetailSectionContentEnterTransition(
                    delayMillis = SharedTransitionAnimations.CONTENT_STAGGER_DELAY_MS,
                ),
                exit = fadeOut(tween(180)),
            ) {
                MovieDetailErrorState(
                    message = uiState.errorMessage.orEmpty(),
                    onRetry = onRetry,
                )
            }
            AnimatedVisibility(
                visible = movieDetail != null,
                enter = sharedDetailSectionContentEnterTransition(
                    delayMillis = SharedTransitionAnimations.OVERLAY_ENTER_DELAY_MS,
                ),
                exit = fadeOut(tween(220)),
            ) {
                if (movieDetail != null) {
                    Column {
                        MovieDetailContent(movieDetail = movieDetail)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun MovieDetailHeroSection(
    movieId: Int,
    movieTitle: String,
    moviePoster: String,
    posterCornerRadiusDp: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBack: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp),
    ) {
        val cornerRadius = rememberAnimatedPosterCornerRadius(
            animatedVisibilityScope = animatedVisibilityScope,
            listCornerRadius = posterCornerRadiusDp.dp,
            isDetailDestination = true,
        )
        with(sharedTransitionScope) {
            AsyncImage(
                model = moviePoster,
                contentDescription = movieTitle,
                modifier = sharedMoviePosterModifier(
                    posterKey = moviePosterKey(movieId),
                    animatedVisibilityScope = animatedVisibilityScope,
                    cornerRadius = cornerRadius,
                ).fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                            MaterialTheme.colorScheme.surface,
                        ),
                        startY = 300f,
                    ),
                ),
        )
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(top = 48.dp, start = 16.dp)
                .align(Alignment.TopStart)
                .then(
                    with(animatedVisibilityScope) {
                        Modifier.animateEnterExit(
                            enter = sharedDetailOverlayEnterTransition(),
                            exit = fadeOut(tween(180)),
                        )
                    },
                )
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.3f)),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 48.dp, end = 16.dp)
                .align(Alignment.TopEnd),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            IconButton(
                onClick = { },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.3f)),
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color.White,
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.5f),
                            Color.Transparent,
                        ),
                    ),
                ),
        )
        Surface(
            modifier = Modifier
                .align(Alignment.Center)
                .size(72.dp)
                .then(
                    with(sharedTransitionScope) {
                        Modifier.renderInSharedTransitionScopeOverlay(
                            renderInOverlay = {
                                shouldRenderSharedOverlay(
                                    currentState = animatedVisibilityScope.transition.currentState,
                                    targetState = animatedVisibilityScope.transition.targetState,
                                )
                            },
                        )
                    },
                )
                .then(
                    with(animatedVisibilityScope) {
                        Modifier.animateEnterExit(
                            enter = sharedDetailPlayButtonEnterTransition(),
                            exit = sharedDetailPlayButtonExitTransition(),
                        )
                    },
                )
                .clickable { },
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
            tonalElevation = 8.dp,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Play Trailer",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@Composable
private fun MovieDetailContent(movieDetail: MovieDetail) {
    Text(
        text = movieDetail.title,
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Black,
        letterSpacing = (-1).sp,
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (movieDetail.imdbRating.isNotBlank()) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(
                        movieDetail.imdbRating,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }
        Text(
            text = buildMovieMetaLine(movieDetail),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
    if (movieDetail.plot.isNotBlank()) {
        Text(
            text = "Summary",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = movieDetail.plot,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 26.sp,
        )
    }
    if (movieDetail.director.isNotBlank()) {
        MovieDetailInfoRow(label = "Director", value = movieDetail.director)
    }
    if (movieDetail.writer.isNotBlank()) {
        MovieDetailInfoRow(label = "Writer", value = movieDetail.writer)
    }
    if (movieDetail.country.isNotBlank()) {
        MovieDetailInfoRow(label = "Country", value = movieDetail.country)
    }
    if (movieDetail.awards.isNotBlank()) {
        MovieDetailInfoRow(label = "Awards", value = movieDetail.awards)
    }
    val castNames: List<String> = movieDetail.actors
        .split(",")
        .map { name -> name.trim() }
        .filter { name -> name.isNotEmpty() }
    if (castNames.isNotEmpty()) {
        Text(
            text = "Cast",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
        ) {
            items(castNames) { actorName ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        modifier = Modifier.size(64.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = actorName.firstOrNull()?.uppercaseChar()?.toString()
                                    .orEmpty(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = actorName,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 2,
                    )
                }
            }
        }
    }
    if (movieDetail.images.isNotEmpty()) {
        Text(
            text = "Gallery",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
        ) {
            items(movieDetail.images) { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .width(200.dp)
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

@Composable
private fun MovieDetailInfoRow(
    label: String,
    value: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun MovieDetailErrorState(
    message: String,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
        )
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

private fun buildMovieMetaLine(movieDetail: MovieDetail): String {
    return listOfNotNull(
        movieDetail.year.takeIf { it.isNotBlank() },
        movieDetail.runtime.takeIf { it.isNotBlank() },
        movieDetail.genres.takeIf { it.isNotEmpty() }?.joinToString(" • "),
    ).joinToString(" • ")
}
