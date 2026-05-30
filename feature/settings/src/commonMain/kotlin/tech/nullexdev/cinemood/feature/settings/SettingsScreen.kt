package tech.nullexdev.cinemood.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tech.nullexdev.cinemood.feature.settings.presentation.SettingsThemeMode
import tech.nullexdev.cinemood.feature.settings.presentation.SettingsUiAction
import tech.nullexdev.cinemood.feature.settings.presentation.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "Theme: ${uiState.themeMode.name}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SettingsThemeMode.entries.forEach { mode ->
            TextButton(
                onClick = { viewModel.onAction(SettingsUiAction.ThemeModeSelected(mode)) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = mode.name,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "Notifications",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Switch(
                checked = uiState.notificationsEnabled,
                onCheckedChange = { enabled ->
                    viewModel.onAction(SettingsUiAction.NotificationsToggled(enabled))
                },
            )
        }
    }
}
