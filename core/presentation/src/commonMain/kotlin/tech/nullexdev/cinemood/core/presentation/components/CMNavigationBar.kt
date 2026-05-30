package tech.nullexdev.cinemood.core.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import tech.nullexdev.cinemood.core.navigation.Screen

@Composable
fun CMNavigationBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = primaryColor,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = currentScreen == Screen.Home,
            onClick = { onNavigate(Screen.Home) },
            label = { Text("Home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Navigation Home Icon") },
            colors = NavigationBarItemDefaults.colors(
                selectedTextColor = primaryColor,
                selectedIconColor = primaryColor,
                indicatorColor = primaryColor.copy(alpha = 0.15f),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
            )
        )
        NavigationBarItem(
            selected = currentScreen == Screen.Search,
            onClick = { onNavigate(Screen.Search) },
            label = { Text("Search") },
            icon = { Icon(Icons.Default.Search, contentDescription = "Navigation Search Icon") },
            colors = NavigationBarItemDefaults.colors(
                selectedTextColor = primaryColor,
                selectedIconColor = primaryColor,
                indicatorColor = primaryColor.copy(alpha = 0.15f),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
            )
        )
        NavigationBarItem(
            selected = currentScreen == Screen.Favorite,
            onClick = { onNavigate(Screen.Favorite) },
            label = { Text("Favorite") },
            icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Navigation Favorite Icon") },
            colors = NavigationBarItemDefaults.colors(
                selectedTextColor = primaryColor,
                selectedIconColor = primaryColor,
                indicatorColor = primaryColor.copy(alpha = 0.15f),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
            )
        )
        NavigationBarItem(
            selected = currentScreen == Screen.Settings,
            onClick = { onNavigate(Screen.Settings) },
            label = { Text("Settings") },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Navigation Settings Icon") },
            colors = NavigationBarItemDefaults.colors(
                selectedTextColor = primaryColor,
                selectedIconColor = primaryColor,
                indicatorColor = primaryColor.copy(alpha = 0.15f),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
            )
        )
    }
}
