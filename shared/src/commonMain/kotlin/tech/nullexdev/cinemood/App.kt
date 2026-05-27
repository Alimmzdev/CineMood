package tech.nullexdev.cinemood

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.nullexdev.cinemood.theme.MyKMPAppTheme
import tech.nullexdev.cinemood.theme.ThemeState


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
@Preview
fun App() {
    val themeState = remember { _root_ide_package_.tech.nullexdev.cinemood.theme.ThemeState() }

    _root_ide_package_.tech.nullexdev.cinemood.theme.MyKMPAppTheme(themeState = themeState) {
        Scaffold(
            topBar = {
                _root_ide_package_.tech.nullexdev.cinemood.CMTopAppBar()
            },
            bottomBar = {
                val selectedItem = remember { mutableIntStateOf(0) }
                _root_ide_package_.tech.nullexdev.cinemood.CMNavigationBar(selectedItem = selectedItem)
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CMTopAppBar() {
    TopAppBar(
        title = { Text("CineMood") },
        actions = {
            Row {
                IconButton(
                    onClick = {

                    },
                    shapes = IconButtonShapes(shape = CircleShape),
                    content = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon"
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {

                    },
                    shapes = IconButtonShapes(shape = CircleShape),
                    content = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite Icon"
                            )
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun CMNavigationBar(
    selectedItem: MutableIntState,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = Color.Red,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = selectedItem.value == 0,
            onClick = {
                selectedItem.value = 0
            },
            label = {
                Text("Home")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Navigation Home Icon"
                )
            },
            colors = NavigationBarItemColors(
                selectedTextColor = Color.Red,
                selectedIconColor = Color.Red,
                selectedIndicatorColor = Color.Red.copy(alpha = 0.15f),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                disabledIconColor = Color.Gray.copy(alpha = 0.2f),
                disabledTextColor = Color.Gray.copy(alpha = 0.2f)
            )
        )
        NavigationBarItem(
            selected = selectedItem.value == 1,
            onClick = {
                selectedItem.value = 1
            },
            label = {
                Text("Search")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Navigation Search Icon"
                )
            },
            colors = NavigationBarItemColors(
                selectedTextColor = Color.Red,
                selectedIconColor = Color.Red,
                selectedIndicatorColor = Color.Red.copy(alpha = 0.15f),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                disabledIconColor = Color.Gray.copy(alpha = 0.2f),
                disabledTextColor = Color.Gray.copy(alpha = 0.2f)
            )
        )
        NavigationBarItem(
            selected = selectedItem.value == 2,
            onClick = {
                selectedItem.value = 2
            },
            label = {
                Text("Favorite")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Navigation Favorite Icon"
                )
            },
            colors = NavigationBarItemColors(
                selectedTextColor = Color.Red,
                selectedIconColor = Color.Red,
                selectedIndicatorColor = Color.Red.copy(alpha = 0.15f),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                disabledIconColor = Color.Gray.copy(alpha = 0.2f),
                disabledTextColor = Color.Gray.copy(alpha = 0.2f)
            )
        )
        NavigationBarItem(
            selected = selectedItem.value == 3,
            colors = NavigationBarItemColors(
                selectedTextColor = Color.Red,
                selectedIconColor = Color.Red,
                selectedIndicatorColor = Color.Red.copy(alpha = 0.15f),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                disabledIconColor = Color.Gray.copy(alpha = 0.2f),
                disabledTextColor = Color.Gray.copy(alpha = 0.2f)
            ),
            onClick = {
                selectedItem.value = 3
            },
            label = {
                Text("Settings")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Navigation Settings Icon"
                )
            }
        )
    }
}