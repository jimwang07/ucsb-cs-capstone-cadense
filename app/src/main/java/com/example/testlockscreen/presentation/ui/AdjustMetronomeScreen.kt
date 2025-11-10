package com.example.testlockscreen.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import com.example.testlockscreen.presentation.viewmodel.SettingsViewModel

@Composable
fun AdjustMetronomeScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val bpm by viewModel.defaultBpm.collectAsState()

    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { viewModel.setDefaultBpm(bpm - 1) }) {
                    Text(text = "-")
                }
                Text(text = "$bpm")
                Button(onClick = { viewModel.setDefaultBpm(bpm + 1) }) {
                    Text(text = "+")
                }
            }
        }
        item {
            Button(onClick = onBack, shape = RoundedCornerShape(12.dp)) {
                Text("Back")
            }
        }
    }
}
