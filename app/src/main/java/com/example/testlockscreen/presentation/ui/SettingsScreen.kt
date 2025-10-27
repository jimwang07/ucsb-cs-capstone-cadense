package com.example.testlockscreen.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    initialBpm: Int,
    onBpmChange: (Int) -> Unit
) {
    var bpm by remember { mutableStateOf(initialBpm) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Settings", modifier = Modifier.padding(bottom = 16.dp))
        Text(text = "BPM: $bpm", modifier = Modifier.padding(bottom = 8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Button(
                onClick = { if (bpm > 1) bpm-- },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "-")
            }
            Button(
                onClick = { bpm++ },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "+")
            }
        }
        Button(
            onClick = {
                onBpmChange(bpm)
                onBack()
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(text = "Save")
        }
        Button(
            onClick = onBack,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Back")
        }
    }
}
