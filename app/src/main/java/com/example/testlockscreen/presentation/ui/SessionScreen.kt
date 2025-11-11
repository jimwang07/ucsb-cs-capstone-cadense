package com.example.testlockscreen.presentation.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.testlockscreen.audio.AudioMetronome
import com.example.testlockscreen.haptics.HapticsController
import com.example.testlockscreen.presentation.viewmodel.MetronomeViewModel
import com.example.testlockscreen.presentation.viewmodel.SettingsViewModel

@Composable
fun SessionScreen(
    metronomeViewModel: MetronomeViewModel,
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val isRunning by metronomeViewModel.isRunning.collectAsState()
    val beatCount by metronomeViewModel.beatCount.collectAsState()
    val stopwatch by metronomeViewModel.stopwatch.collectAsState()
    val bpm by metronomeViewModel.bpm.collectAsState()

    val isVisualEnabled by settingsViewModel.isVisualEnabled.collectAsState()
    val isAudioEnabled by settingsViewModel.isAudioEnabled.collectAsState()
    val isVibrationEnabled by settingsViewModel.isVibrationEnabled.collectAsState()

    val context = LocalContext.current
    val hapticsController = remember { HapticsController(context) }
    val audioMetronome = remember { AudioMetronome() }

    val backgroundColor = if (isRunning && isVisualEnabled && beatCount % 2 == 0) {
        Color.Blue
    } else {
        Color.Black
    }

    // Haptics
    LaunchedEffect(beatCount) {
        if (isRunning && isVibrationEnabled && beatCount > 0) {
            Log.d("SessionScreen", "Vibrating...")
            hapticsController.vibrate(30)
        }
    }

    // Audio
    LaunchedEffect(beatCount) {
        if (isRunning && isAudioEnabled && beatCount > 0) {
            audioMetronome.playBeep()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            hapticsController.cancel()
            audioMetronome.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = metronomeViewModel.formatStopwatch(stopwatch))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { metronomeViewModel.toggle() }, shape = RoundedCornerShape(12.dp)) {
                    Icon(
                        imageVector = if (isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isRunning) "Pause" else "Play"
                    )
                }
                Button(
                    onClick = { metronomeViewModel.stop() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Icon(imageVector = Icons.Filled.Stop, contentDescription = "Stop")
                }
            }
            Text(text = "BPM: $bpm")
        }

        // Custom-built Icon Button as a workaround
        Button(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 4.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colors.primary
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
    }
}
