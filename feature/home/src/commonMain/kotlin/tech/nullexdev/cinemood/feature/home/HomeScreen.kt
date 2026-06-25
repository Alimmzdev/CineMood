package tech.nullexdev.cinemood.feature.home

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.koin.compose.viewmodel.koinViewModel
import tech.nullexdev.cinemood.core.presentation.components.ErrorState
import tech.nullexdev.cinemood.core.presentation.components.SharedMoviePosterDefaults
import tech.nullexdev.cinemood.core.presentation.components.moviePosterKey
import tech.nullexdev.cinemood.core.presentation.components.rememberAnimatedPosterCornerRadius
import tech.nullexdev.cinemood.core.presentation.components.sharedMoviePosterModifier
import tech.nullexdev.cinemood.feature.home.presentation.HomeUiAction
import tech.nullexdev.cinemood.feature.home.presentation.HomeUiState
import tech.nullexdev.cinemood.feature.home.presentation.HomeViewModel
import tech.nullexdev.cinemood.service.domain.model.Movie

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    onMovieClick: (Movie) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    HomeContent(
        uiState = uiState,
        scrollBehavior = scrollBehavior,
        onMovieClick = onMovieClick,
        onAction = viewModel::onAction,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun HomeContent(
    uiState: HomeUiState,
    scrollBehavior: TopAppBarScrollBehavior,
    onMovieClick: (Movie) -> Unit,
    onAction: (HomeUiAction) -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(
                            "Discover",
                            fontWeight = FontWeight.Black,
                            fontSize = 34.sp
                        )
                        Text(
                            "Find your next favorite movie",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onAction(HomeUiAction.SearchClicked) },
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    Surface(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable { /* Profile */ },
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
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
                uiState.isLoading && uiState.movies.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(strokeCap = androidx.compose.ui.graphics.StrokeCap.Round)
                    }
                }

                uiState.errorMessage != null && uiState.movies.isEmpty() -> {
                    ErrorState(
                        message = uiState.errorMessage,
                        onRetry = { onAction(HomeUiAction.Refresh) },
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                else -> {
                    val featuredMovies = uiState.movies.take(5)
                    val remainingMovies = uiState.movies.drop(5)
                    val genres = listOf("All", "Action", "Drama", "Sci-Fi", "Comedy", "Horror", "Thriller")

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Featured Section
                        if (featuredMovies.isNotEmpty()) {
                            item {
                                FeaturedCarousel(
                                    movies = featuredMovies.toImmutableList(),
                                    onMovieClick = { onMovieClick(it) }
                                )
                            }
                        }

                        // Categories / Genres
                        item {
                            GenreSelector(genres)
                        }

                        // Remaining Movies Header
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Trending Now",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                TextButton(onClick = {}) {
                                    Text("See all")
                                }
                            }
                        }

                        // Vertical Grid (Vertical posters)
                        remainingMovies.chunked(2).forEach { rowMovies ->
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    rowMovies.forEach { movie ->
                                        Box(modifier = Modifier.weight(1f)) {
                                            VerticalMovieCard(
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

                        // Pagination
                        if (uiState.hasNextPage) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (uiState.isLoading) {
                                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                                    } else {
                                        Button(
                                            onClick = { onAction(HomeUiAction.LoadNextPage) },
                                            shape = RoundedCornerShape(16.dp)
                                        ) {
                                            Text("Explore More")
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
}

@Composable
fun FeaturedCarousel(
    movies: ImmutableList<Movie>,
    onMovieClick: (Movie) -> Unit = {},
) {
    val pagerState = rememberPagerState(pageCount = { movies.size })

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 24.dp),
            pageSpacing = 16.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val movie = movies[page]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clickable { onMovieClick(movie) },
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Box {
                    AsyncImage(
                        model = movie.poster,
                        contentDescription = movie.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                                    startY = 100f
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(24.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "FEATURED",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            movie.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
        
        Row(
            Modifier
                .height(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(movies.size) { iteration ->
                val isSelected = pagerState.currentPage == iteration
                val indicatorWidth by animateDpAsState(
                    targetValue = if (isSelected) 18.dp else 6.dp,
                    animationSpec = tween(durationMillis = 250),
                    label = "indicatorWidth"
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        )
                        .size(width = indicatorWidth, height = 6.dp)
                )
            }
        }
    }
}

@Composable
fun GenreSelector(genres: List<String>) {
    var selectedGenre by remember { mutableStateOf(genres.first()) }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(genres) { genre ->
            val isSelected = selectedGenre == genre
            FilterChip(
                selected = isSelected,
                onClick = { selectedGenre = genre },
                label = { Text(genre) },
                shape = RoundedCornerShape(16.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = null
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun VerticalMovieCard(
    movie: Movie,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f), // Vertical aspect ratio (roughly 2:3)
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            val modifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
                val cornerRadius = rememberAnimatedPosterCornerRadius(
                    animatedVisibilityScope = animatedVisibilityScope,
                    listCornerRadius = SharedMoviePosterDefaults.verticalCardCornerRadius,
                    isDetailDestination = false,
                )
                with(sharedTransitionScope) {
                    sharedMoviePosterModifier(
                        posterKey = moviePosterKey(movie.id),
                        animatedVisibilityScope = animatedVisibilityScope,
                        cornerRadius = cornerRadius,
                    )
                }
            } else {
                Modifier
            }
            AsyncImage(
                model = movie.poster,
                contentDescription = movie.title,
                modifier = modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = movie.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Text(
            text = movie.genres.firstOrNull() ?: "Unknown",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun HomeContentPreview() {
    val movies = persistentListOf(
        Movie(1, "Movie 1", "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDp92SMRYwT6C7R2R9V.jpg", persistentListOf("Action", "Drama"), persistentListOf()),
        Movie(2, "Movie 2", "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDp92SMRYwT6C7R2R9V.jpg", persistentListOf("Comedy"), persistentListOf()),
        Movie(3, "Movie 3", "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDp92SMRYwT6C7R2R9V.jpg", persistentListOf("Thriller"), persistentListOf()),
        Movie(4, "Movie 4", "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDp92SMRYwT6C7R2R9V.jpg", persistentListOf("Sci-Fi"), persistentListOf()),
        Movie(5, "Movie 5", "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDp92SMRYwT6C7R2R9V.jpg", persistentListOf("Horror"), persistentListOf()),
        Movie(6, "Movie 6", "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDp92SMRYwT6C7R2R9V.jpg", persistentListOf("Romance"), persistentListOf())
    )
    val uiState = HomeUiState(
        movies = movies,
        isLoading = false,
        hasNextPage = true
    )
    MaterialTheme {
        HomeContent(
            uiState = uiState,
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
            onMovieClick = {},
            onAction = {}
        )
    }
}
