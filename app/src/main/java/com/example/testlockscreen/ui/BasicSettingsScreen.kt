package com.example.testlockscreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.AutoCenteringParams
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.items
import androidx.wear.compose.material.rememberScalingLazyListState
import com.example.testlockscreen.model.VisualColorOption
import com.example.testlockscreen.model.VisualColorOptions
import com.example.testlockscreen.viewmodel.SettingsViewModel
import com.example.testlockscreen.ui.HomeChip

@Composable
fun BasicSettingsScreen(
    onHome: () -> Unit,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val defaultBpm by settingsViewModel.defaultBpm.collectAsState()
    val visualColor by settingsViewModel.visualColor.collectAsState()

    var bpm by remember(defaultBpm) { mutableIntStateOf(defaultBpm) }
    var selectedColor by remember(visualColor) { mutableStateOf(visualColor) }

    val listState = rememberScalingLazyListState()

    ScalingLazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        autoCentering = AutoCenteringParams(itemIndex = 0)
    ) {
        item {
            HomeChip(
                onHome = onHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }
        item {
            ListHeader {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.title1,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary
                )
            }
        }
        item {
            Text(
                text = "Default BPM",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
        }
        item {
            Text(
                text = "$bpm",
                style = MaterialTheme.typography.display1,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AdjustmentChip(label = "-5", modifier = Modifier.width(64.dp)) { bpm = (bpm - 5).coerceAtLeast(40) }
                AdjustmentChip(label = "-1", modifier = Modifier.width(64.dp)) { bpm = (bpm - 1).coerceAtLeast(40) }
                AdjustmentChip(label = "+1", modifier = Modifier.width(64.dp)) { bpm = (bpm + 1).coerceAtMost(200) }
                AdjustmentChip(label = "+5", modifier = Modifier.width(64.dp)) { bpm = (bpm + 5).coerceAtMost(200) }
            }
        }
        item {
            Text(
                text = "Visual Flash Color",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
        items(VisualColorOptions.All) { option ->
            ColorChoiceChip(
                option = option,
                selected = option == selectedColor,
                onSelect = { selectedColor = option }
            )
        }
        item {
            Chip(
                onClick = {
                    settingsViewModel.saveSettings(bpm, selectedColor)
                    onHome()
                },
                label = { Text("Save Settings") },
                modifier = Modifier.fillMaxWidth(),
                colors = ChipDefaults.primaryChipColors()
            )
        }
    }
}

@Composable
private fun ColorChoiceChip(
    option: VisualColorOption,
    selected: Boolean,
    onSelect: () -> Unit
) {
    val background = option.color.copy(alpha = if (selected) 0.8f else 0.4f)
    val contentColor = if (background.luminance() > 0.5f) Color.Black else Color.White
    Chip(
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(option.color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = option.name)
            }
        },
        secondaryLabel = if (selected) {
            { Text(text = "Selected") }
        } else null,
        colors = ChipDefaults.chipColors(
            backgroundColor = background,
            contentColor = contentColor
        )
    )
}

@Composable
private fun AdjustmentChip(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Chip(
        onClick = onClick,
        label = { Text(label) },
        colors = ChipDefaults.secondaryChipColors(),
        modifier = modifier
    )
}
