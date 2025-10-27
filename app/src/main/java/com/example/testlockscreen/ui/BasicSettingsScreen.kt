package com.example.testlockscreen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.Text
import com.example.testlockscreen.viewmodel.SettingsViewModel

@Composable
fun BasicSettingsScreen(
    onDone: () -> Unit,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val defaultBpm by settingsViewModel.defaultBpm.collectAsState()
    val defaultInterval by settingsViewModel.defaultVisualCueInterval.collectAsState()
    val hapticsEnabled by settingsViewModel.hapticsEnabled.collectAsState()
    val textScale by settingsViewModel.textScale.collectAsState()

    var bpm by remember(defaultBpm) { mutableIntStateOf(defaultBpm) }
    var interval by remember(defaultInterval) { mutableFloatStateOf(defaultInterval) }
    var haptics by remember(hapticsEnabled) { mutableStateOf(hapticsEnabled) }
    var scale by remember(textScale) { mutableFloatStateOf(textScale) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Settings", style = MaterialTheme.typography.title1)

        // BPM Stepper
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Default BPM")
            Text("$bpm")
        }
        Slider(
            value = bpm.toFloat(),
            onValueChange = { bpm = it.toInt() },
            valueRange = 40f..120f,
            steps = 79
        )

        // Interval Stepper
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Default Interval")
            Text("%.1f s".format(interval))
        }
        Slider(
            value = interval,
            onValueChange = { interval = it },
            valueRange = 0.5f..3.0f,
            steps = 4
        )

        // Haptics Toggle
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Haptics")
            Switch(checked = haptics, onCheckedChange = { haptics = it })
        }

        // Text Scale Slider
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Text Scale")
            Text("%.1fx".format(scale))
        }
        Slider(
            value = scale,
            onValueChange = { scale = it},
            valueRange = 1.0f..1.5f,
            steps = 4
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                settingsViewModel.saveSettings(bpm, interval, haptics, scale)
                onDone()
            }
        ) {
            Text("Done")
        }
    }
}
