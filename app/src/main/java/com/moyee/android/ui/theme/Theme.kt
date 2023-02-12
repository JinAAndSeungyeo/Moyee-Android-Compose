package com.moyee.android.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MoyeeColors = darkColors(
    primary = Green500,
    onPrimary = Purple900,
    secondary = Purple400,
    onSecondary = Color.White,
    secondaryVariant = Purple50,
    surface = Purple800,
    onSurface = Color.White,
    background = Purple900,
)

@Composable
fun MoyeeTheme(content: @Composable () -> Unit) {
    // default 가 다크 모드
    MaterialTheme(
        colors = MoyeeColors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}