package com.example.testlockscreen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.example.testlockscreen.R
import com.example.testlockscreen.haptics.HapticsController

@Composable
fun MetronomeTrainingScreen(
    navController: NavHostController
) {
    var bpm by remember { mutableStateOf(60) }
    var isRecording by remember { mutableStateOf(false) }
    val hapticsEnabled by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val hapticsController = HapticsController(context)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${bpm} BPM",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { bpm = (bpm - 1).coerceAtLeast(40) }) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease BPM")
            }
            Button(onClick = { bpm = 60 }) {
                Text(text = "60")
            }
            Button(onClick = { bpm = (bpm + 1).coerceAtMost(120) }) {
                Icon(Icons.Default.Add, contentDescription = "Increase BPM")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { 
                    isRecording = true
                    if (hapticsEnabled) {
                        // This needs to be scheduled based on BPM
                        // hapticsController.playMetronomeBeat()
                    }
                },
                enabled = !isRecording
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Start")
                Text(text = stringResource(R.string.start))
            }

            Button(
                onClick = { 
                    isRecording = false
                    hapticsController.cancelHaptics()
                },
                enabled = isRecording
            ) {
                Icon(Icons.Default.Stop, contentDescription = "Stop")
                Text(text = stringResource(R.string.stop))
            }
        }
    }
}
