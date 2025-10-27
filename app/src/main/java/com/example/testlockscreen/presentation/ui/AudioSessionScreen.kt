package com.example.testlockscreen.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.example.testlockscreen.audio.AudioMetronome
import com.example.testlockscreen.presentation.viewmodel.MetronomeViewModel

@Composable
fun AudioSessionScreen(
    viewModel: MetronomeViewModel,
    onBack: () -> Unit
) {
    val isRunning by viewModel.isRunning.collectAsState()
    val beatCount by viewModel.beatCount.collectAsState()
    val stopwatch by viewModel.stopwatch.collectAsState()
    val bpm by viewModel.bpm.collectAsState()

    val audioMetronome = remember { AudioMetronome() }

    LaunchedEffect(beatCount) {
        if (isRunning && beatCount > 0) {
            audioMetronome.playBeep()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            audioMetronome.release()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = viewModel.formatStopwatch(stopwatch)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.toggle() }, shape = RoundedCornerShape(12.dp)) {
                Icon(
                    imageVector = if (isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (isRunning) "Pause" else "Play"
                )
            }
            Button(
                onClick = { viewModel.stop() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            ) {
                Icon(
                    imageVector = Icons.Filled.Stop,
                    contentDescription = "Stop"
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "BPM: $bpm")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { viewModel.setBpm(bpm - 1) }, shape = RoundedCornerShape(12.dp)) {
                    Text(text = "-")
                }
                Button(onClick = { viewModel.setBpm(bpm + 1) }, shape = RoundedCornerShape(12.dp)) {
                    Text(text = "+")
                }
            }
        }
        Button(onClick = onBack, shape = RoundedCornerShape(12.dp)) {
            Text(text = "Back")
        }
    }
}
