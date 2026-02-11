package com.example.stride.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.example.stride.audio.AudioMetronome
import com.example.stride.audio.AudioPrompts
import com.example.stride.haptics.HapticsController
import com.example.stride.presentation.viewmodel.SettingsViewModel
import com.example.stride.sensors.PoleStrikeDetector
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun CalibrationScreen(
    settingsViewModel: SettingsViewModel,
    onCalibrationComplete: (bpm: Int) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val isAudioEnabled by settingsViewModel.isAudioEnabled.collectAsState()
    val isVibrationEnabled by settingsViewModel.isVibrationEnabled.collectAsState()
    val defaultBpm by settingsViewModel.defaultBpm.collectAsState()

    var isAudioReady by remember { mutableStateOf(false) }
    val audioPrompts = remember {
        AudioPrompts(context, onAllLoaded = { isAudioReady = true })
    }

    val audioMetronome = remember { AudioMetronome(context) }
    val hapticsController = remember { HapticsController(context) }

    val poleStrikeDetector = remember { PoleStrikeDetector(context, coroutineScope) }
    
    var strikeCount by remember { mutableIntStateOf(0) }
    val strikeTimestamps = remember { mutableStateListOf<Long>() }
    
    var currentBpm by remember { mutableIntStateOf(defaultBpm * 2) }
    var beatCount by remember { mutableIntStateOf(0) }
    var metronomeStarted by remember { mutableStateOf(false) }

    LaunchedEffect(isAudioReady) {
        if (isAudioReady) {
            audioPrompts.play(AudioPrompts.Prompt.CALIBRATING_PACE)
            delay(2000) // Give time for the "Calibrating pace" prompt
            metronomeStarted = true
        }
    }

    // Metronome loop during calibration that adapts to currentBpm
    LaunchedEffect(metronomeStarted) {
        if (metronomeStarted) {
            while (isActive) {
                beatCount++
                if (isVibrationEnabled) {
                    hapticsController.vibrate(30)
                }
                if (isAudioEnabled) {
                    val beatInBar = ((beatCount - 1) % 4) + 1
                    audioMetronome.playBeat(beatInBar)
                }
                delay(60000L / currentBpm.coerceAtLeast(1))
            }
        }
    }

    LaunchedEffect(Unit) {
        poleStrikeDetector.startDetection()
        poleStrikeDetector.strikeEvents.collect { event ->
            if (strikeCount < 10) {
                strikeTimestamps.add(event.timestamp)
                strikeCount++
                
                if (strikeCount >= 2) {
                    // Update BPM adaptively based on all strikes so far
                    val totalDurationMs = strikeTimestamps.last() - strikeTimestamps.first()
                    val avgIntervalMs = totalDurationMs / (strikeCount - 1)
                    if (avgIntervalMs > 0) {
                        val detectedBpm = (60000 / avgIntervalMs).toInt()
                        currentBpm = detectedBpm * 2
                    }
                }
                
                if (strikeCount == 10) {
                    onCalibrationComplete(currentBpm)
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            audioPrompts.release()
            audioMetronome.release()
            hapticsController.cancel()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Please start walking at your target pace.",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Beats detected: $strikeCount / 10",
            fontSize = 14.sp,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Current BPM: $currentBpm",
            fontSize = 14.sp,
            color = Color.Cyan
        )
    }
}
