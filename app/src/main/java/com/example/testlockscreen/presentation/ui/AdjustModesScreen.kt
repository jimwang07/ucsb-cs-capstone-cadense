package com.example.testlockscreen.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.SwitchDefaults
import androidx.wear.compose.material.Text
import com.example.testlockscreen.presentation.viewmodel.SettingsViewModel

@Composable
fun AdjustModesScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val isVisualEnabled by viewModel.isVisualEnabled.collectAsState()
    val isAudioEnabled by viewModel.isAudioEnabled.collectAsState()
    val isVibrationEnabled by viewModel.isVibrationEnabled.collectAsState()

    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Visual")
                Switch(
                    checked = isVisualEnabled,
                    onCheckedChange = { viewModel.setVisualEnabled(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colors.primary
                    )
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Audio")
                Switch(
                    checked = isAudioEnabled,
                    onCheckedChange = { viewModel.setAudioEnabled(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colors.primary
                    )
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Vibration")
                Switch(
                    checked = isVibrationEnabled,
                    onCheckedChange = { viewModel.setVibrationEnabled(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colors.primary
                    )
                )
            }
        }
        item {
            Button(onClick = onBack, shape = RoundedCornerShape(12.dp)) {
                Text("Back")
            }
        }
    }
}
