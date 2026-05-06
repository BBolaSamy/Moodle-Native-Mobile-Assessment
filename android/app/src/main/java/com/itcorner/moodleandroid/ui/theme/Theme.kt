package com.itcorner.moodleandroid.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = MoodleBlue,
    secondary = MoodleBlueDark,
    background = GroupedBackground,
    surface = RowSurface,
    surfaceVariant = GroupedBackground
)

private val DarkColors = darkColorScheme(
    primary = MoodleBlue
)

@Composable
fun MoodleAndroidTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content
    )
}
