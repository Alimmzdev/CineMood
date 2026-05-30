package tech.nullexdev.cinemood.feature.search

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import tech.nullexdev.cinemood.core.presentation.components.ErrorState
import tech.nullexdev.cinemood.core.presentation.components.MovieCard
import tech.nullexdev.cinemood.feature.search.presentation.SearchUiAction
import tech.nullexdev.cinemood.feature.search.presentation.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listState = rememberLazyListState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    scrolledContainerColor =  MaterialTheme.colorScheme.surface,
                ),
                title = {
                    Column {
                        Text(
                            text = "Discover",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Find your next favorite movie",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPaddings ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPaddings.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Search Bar
            stickyHeader {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    OutlinedTextField(
                        value = state.query,
                        onValueChange = {
                            viewModel.onAction(SearchUiAction.QueryChanged(it))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(28.dp),
                        singleLine = true,
                        placeholder = { Text("Search movies...") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null)
                        },
                        trailingIcon = {
                            AnimatedVisibility(
                                visible = state.query.isNotEmpty(),
                                enter = fadeIn() + scaleIn(),
                                exit = fadeOut() + scaleOut()
                            ) {
                                IconButton(onClick = { viewModel.onAction(SearchUiAction.ClearQuery) }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear"
                                    )
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        )
                    )
                }
            }

            // 2. Filters
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SearchFilters()
                }
            }

            // 3. State-aware Content
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    AnimatedContent(
                        targetState = state,
                        transitionSpec = {
                            fadeIn() togetherWith fadeOut()
                        },
                        label = "SearchState"
                    ) { targetState ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            when {
                                targetState.isLoading -> {
                                    LoadingState()
                                }

                                targetState.errorMessage != null -> {
                                    ErrorState(
                                        message = targetState.errorMessage,
                                        onRetry = { viewModel.onAction(SearchUiAction.SearchSubmitted) },
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp)
                                    )
                                }

                                targetState.hasSearched && targetState.movies.isEmpty() -> {
                                    EmptySearchState()
                                }

                                targetState.movies.isNotEmpty() -> {
                                    Text(
                                        text = "${targetState.movies.size} results",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                }

                                else -> {
                                    WelcomeState()
                                }
                            }
                        }
                    }
                }
            }

            // 4. Results List (Flattened into the same LazyColumn)
            if (!state.isLoading && state.movies.isNotEmpty()) {
                items(
                    items = state.movies,
                    key = { it.id }
                ) { movie ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        MovieCard(
                            movie = movie,
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchFilters() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = true,
            onClick = {},
            label = { Text("Popular") },
            shape = RoundedCornerShape(12.dp)
        )
        FilterChip(
            selected = false,
            onClick = {},
            label = { Text("Trending") },
            shape = RoundedCornerShape(12.dp)
        )
        FilterChip(
            selected = false,
            onClick = {},
            label = { Text("Top Rated") },
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptySearchState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Movie,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "No movies found", style = MaterialTheme.typography.titleMedium)
        Text(text = "Try another movie title", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun WelcomeState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Movie,
            contentDescription = null,
            modifier = Modifier.size(96.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Search Movies",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(text = "Start typing to discover movies", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
