package com.example.testlockscreen.model

import androidx.compose.ui.graphics.Color

data class VisualColorOption(
    val name: String,
    val color: Color,
    val preferenceValue: String
)

object VisualColorOptions {
    val All = listOf(
        VisualColorOption("Cyan", Color(0xFF00BCD4), "cyan"),
        VisualColorOption("Blue", Color(0xFF2196F3), "blue"),
        VisualColorOption("Purple", Color(0xFF9C27B0), "purple"),
        VisualColorOption("Green", Color(0xFF4CAF50), "green")
    )

    fun fromPreference(value: String): VisualColorOption =
        All.firstOrNull { it.preferenceValue == value } ?: All.first()
}
