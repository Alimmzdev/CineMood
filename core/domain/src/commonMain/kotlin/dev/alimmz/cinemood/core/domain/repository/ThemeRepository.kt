package dev.alimmz.cinemood.core.domain.repository

import kotlinx.coroutines.flow.StateFlow
import dev.alimmz.cinemood.core.domain.entity.ThemeMode

interface ThemeRepository {
    val themeMode: StateFlow<ThemeMode>
    suspend fun setThemeMode(mode: ThemeMode)
}
