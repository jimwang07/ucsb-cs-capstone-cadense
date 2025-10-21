package com.example.testlockscreen.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
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
import com.example.testlockscreen.navigation.Screen
import com.example.testlockscreen.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun VisualCueTrainingScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = viewModel()
) {
    val bpm by mainViewModel.bpm.collectAsState()
    val visualColor by mainViewModel.visualColor.collectAsState()
    val sessionState by mainViewModel.sessionState.collectAsState()
    val isRecording by mainViewModel.isRecording.collectAsState()
    val connectionState by mainViewModel.connectionState.collectAsState()

    var showVisualCue by remember { mutableStateOf(false) }

    LaunchedEffect(sessionState, bpm, visualColor) {
        showVisualCue = false
        if (sessionState == MainViewModel.SessionState.Running) {
            val delayMs = (60_000.0 / bpm).toLong().coerceAtLeast(200L)
            while (isActive) {
                showVisualCue = true
                delay(delayMs / 2)
                showVisualCue = false
                delay(delayMs / 2)
            }
        }
    }

    val backgroundColor by animateColorAsState(
        targetValue = if (showVisualCue) visualColor.color else Color.Black,
        animationSpec = tween(durationMillis = 150),
        label = "visualCueColor"
    )

    val listState = rememberScalingLazyListState()
    val textColor = if (backgroundColor.luminance() > 0.5f) Color.Black else Color.White

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Visual Metronome",
                            style = MaterialTheme.typography.title1,
                            textAlign = TextAlign.Center,
                            color = textColor
                        )
                        Text(
                            text = "$bpm BPM",
                            style = MaterialTheme.typography.display1,
                            textAlign = TextAlign.Center,
                            color = textColor
                        )
                        Text(
                            text = "Flashing between black and ${visualColor.name}.",
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center,
                            color = textColor
                        )
                        Text(
                            text = "Press start to begin.",
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center,
                            color = textColor
                        )
                    }
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
                            navController.navigate(Screen.EndSession.route) {
                                popUpTo(Screen.Landing.route) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}
