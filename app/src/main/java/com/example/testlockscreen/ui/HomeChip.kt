package com.example.testlockscreen.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text

@Composable
fun HomeChip(
    onHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Chip(
        onClick = onHome,
        label = { Text("Home") },
        colors = ChipDefaults.secondaryChipColors(),
        modifier = modifier
    )
}
