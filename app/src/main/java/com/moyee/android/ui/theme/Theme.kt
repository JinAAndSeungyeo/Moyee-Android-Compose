package com.moyee.android.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable

private val GreenColors = darkColors(
    primary = Green500,
    surface = Purple800,
    background = Purple900
)

@Composable
fun MoyeeTheme(content: @Composable () -> Unit) {
    // default 가 다크 모드
    MaterialTheme(
        colors = GreenColors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}