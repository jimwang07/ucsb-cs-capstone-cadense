package com.example.testlockscreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.rememberScalingLazyListState
import com.example.testlockscreen.audio.AudioMetronome
import com.example.testlockscreen.navigation.Screen
import com.example.testlockscreen.viewmodel.MainViewModel

@Composable
fun MetronomeTrainingScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = viewModel()
) {
    val bpm by mainViewModel.bpm.collectAsState()
    val sessionState by mainViewModel.sessionState.collectAsState()
    val isRecording by mainViewModel.isRecording.collectAsState()
    val connectionState by mainViewModel.connectionState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val metronome = remember { AudioMetronome() }

    DisposableEffect(Unit) {
        onDispose { metronome.stop() }
    }

    LaunchedEffect(sessionState, bpm) {
        if (sessionState == MainViewModel.SessionState.Running) {
            metronome.start(coroutineScope, bpm)
        } else {
            metronome.stop()
        }
    }

    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            state = listState,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                StatusIndicatorChip(
                    isRecording = isRecording,
                    connectionState = connectionState
                )
            }
            item {
                Text(
                    text = "Auditory Metronome",
                    style = MaterialTheme.typography.title1,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary
                )
            }
            item {
                Text(
                    text = "$bpm BPM",
                    style = MaterialTheme.typography.display1,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onBackground
                )
            }
            item {
                Text(
                    text = "Press start to begin the beat.",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onSurface
                )
            }
            item {
                SessionControlsBar(
                    state = sessionState,
                    onHome = {
                        navController.navigate(Screen.Landing.route) {
                            popUpTo(Screen.Landing.route) { inclusive = true }
                        }
                    },
                    onStart = { mainViewModel.startSession() },
                    onStop = { mainViewModel.stopSession() },
                    onEnd = {
                        mainViewModel.endSession()
                        metronome.stop()
                        navController.navigate(Screen.EndSession.route) {
                            popUpTo(Screen.Landing.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
