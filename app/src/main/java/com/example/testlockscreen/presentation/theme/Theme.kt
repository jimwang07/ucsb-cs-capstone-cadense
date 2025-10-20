package com.example.testlockscreen.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme

val wearColorPalette = Colors(
    background = Color.Black,
    onBackground = Color.White,
    primary = Color(0xFF4CAF50),
    onPrimary = Color.Black,
    secondary = Color(0xFF4CAF50),
    onSecondary = Color.Black,
    error = Color(0xFFF44336),
    onError = Color.White,
    surface = Color.Black,
    onSurface = Color.White
)

val wearTypography = androidx.wear.compose.material.Typography(
    display1 = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    ),
    title1 = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    ),
    body1 = TextStyle(
        fontSize = 20.sp
    ),
    button = TextStyle(
        fontSize = 20.sp
    )
)

@Composable
fun TestLockScreenTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = wearColorPalette,
        typography = wearTypography,
        content = content
    )
}
