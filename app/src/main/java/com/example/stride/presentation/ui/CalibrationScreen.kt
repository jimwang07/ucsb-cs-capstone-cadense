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
import com.example.stride.audio.AudioPrompts
import com.example.stride.sensors.PoleStrikeDetector
import kotlinx.coroutines.delay

@Composable
fun CalibrationScreen(
    onCalibrationComplete: (bpm: Int) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isAudioReady by remember { mutableStateOf(false) }
    val audioPrompts = remember {
        AudioPrompts(context, onAllLoaded = { isAudioReady = true })
    }

    val poleStrikeDetector = remember { PoleStrikeDetector(context, coroutineScope) }
    
    var strikeCount by remember { mutableIntStateOf(0) }
    var isDone by remember { mutableStateOf(false) }
    val strikeTimestamps = remember { mutableStateListOf<Long>() }

    LaunchedEffect(isAudioReady) {
        if (isAudioReady) {
            audioPrompts.play(AudioPrompts.Prompt.CALIBRATING_PACE)
        }
    }

    LaunchedEffect(Unit) {
        poleStrikeDetector.startDetection()
        poleStrikeDetector.strikeEvents.collect { event ->
            if (strikeCount < 10) {
                strikeTimestamps.add(event.timestamp)
                strikeCount++
                
                if (strikeCount == 10) {
                    isDone = true
                    // Calculate BPM based on 10 strikes (9 intervals)
                    val totalDurationMs = strikeTimestamps.last() - strikeTimestamps.first()
                    val avgIntervalMs = totalDurationMs / (strikeCount - 1)
                    val calculatedBpm = (60000 / avgIntervalMs).toInt()
                    
                    delay(2000) // Show "Calibration Done" for 2 seconds
                    onCalibrationComplete(calculatedBpm)
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            audioPrompts.release()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!isDone) {
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
        } else {
            Text(
                text = "Calibration Done!\nBeginning Session...",
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                color = Color.Green
            )
        }
    }
}
