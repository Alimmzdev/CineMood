package dev.alimmz.cinemood.core.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.alimmz.cinemood.core.navigation.Screen

@Composable
fun CMNavigationRail(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable { mutableStateOf(true) }
    val railWidth by animateDpAsState(targetValue = if (isExpanded) 200.dp else 72.dp)
    val primaryColor = MaterialTheme.colorScheme.primary
    
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical + WindowInsetsSides.Start)),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .width(railWidth)
                .fillMaxHeight()
                .padding(vertical = 24.dp, horizontal = 12.dp),
            horizontalAlignment = if (isExpanded) Alignment.Start else Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                )
            }

            NavigationRailItemRow(
                selected = currentScreen == Screen.Home,
                onClick = { onNavigate(Screen.Home) },
                icon = Icons.Default.Home,
                label = "Home",
                primaryColor = primaryColor,
                isExpanded = isExpanded
            )

            Spacer(Modifier.height(12.dp))

            NavigationRailItemRow(
                selected = currentScreen == Screen.Search,
                onClick = { onNavigate(Screen.Search) },
                icon = Icons.Default.Search,
                label = "Search",
                primaryColor = primaryColor,
                isExpanded = isExpanded
            )

            Spacer(Modifier.height(12.dp))

            NavigationRailItemRow(
                selected = currentScreen == Screen.Favorite,
                onClick = { onNavigate(Screen.Favorite) },
                icon = Icons.Default.FavoriteBorder,
                label = "Favorite",
                primaryColor = primaryColor,
                isExpanded = isExpanded
            )

            Spacer(Modifier.height(12.dp))

            NavigationRailItemRow(
                selected = currentScreen == Screen.Settings,
                onClick = { onNavigate(Screen.Settings) },
                icon = Icons.Default.Settings,
                label = "Settings",
                primaryColor = primaryColor,
                isExpanded = isExpanded
            )
        }
    }
}

@Composable
private fun NavigationRailItemRow(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
    primaryColor: Color,
    isExpanded: Boolean
) {
    val backgroundColor = if (selected) primaryColor.copy(alpha = 0.12f) else Color.Transparent
    val contentColor = if (selected) primaryColor else Color.Gray

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge,
                        color = contentColor,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
