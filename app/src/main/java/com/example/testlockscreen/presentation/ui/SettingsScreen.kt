package com.example.testlockscreen.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text

@Composable
fun SettingsScreen(
    onAdjustMetronome: () -> Unit,
    onAdjustModes: () -> Unit,
    onBack: () -> Unit
) {
    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        item {
            Text(text = "Settings")
        }
        item {
            Button(
                onClick = onAdjustMetronome,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Adjust Metronome")
            }
        }
        item {
            Button(
                onClick = onAdjustModes,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Adjust Modes")
            }
        }
        item {
            Button(onClick = onBack, shape = RoundedCornerShape(12.dp)) {
                Text("Back")
            }
        }
    }
}
