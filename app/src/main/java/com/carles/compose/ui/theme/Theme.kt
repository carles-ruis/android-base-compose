package com.carles.compose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun HyruleTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightPalette
    } else {
        DarkPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typo,
        shapes = AppShapes,
        content = content,
    )
}