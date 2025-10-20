package com.example.testlockscreen.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.example.testlockscreen.navigation.Screen
import com.example.testlockscreen.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VisualCueTrainingScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = viewModel()
) {
    val interval by mainViewModel.interval.collectAsState()
    val sessionState by mainViewModel.sessionState.collectAsState()
    val isRecording by mainViewModel.isRecording.collectAsState()
    val connectionState by mainViewModel.connectionState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    var showVisualCue by remember { mutableStateOf(false) }

    LaunchedEffect(sessionState, interval) {
        if (sessionState == MainViewModel.SessionState.Running) {
            while (true) {
                showVisualCue = !showVisualCue
                delay((interval * 1000).toLong())
            }
        } else {
            showVisualCue = false
        }
    }

    val backgroundColor by animateFloatAsState(
        targetValue = if (showVisualCue) 1f else 0f,
        animationSpec = tween(durationMillis = 250)
    )

    val focusRequester = remember { FocusRequester() }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TimeText()
            StatusIndicatorChip(
                isRecording = isRecording,
                connectionState = connectionState
            )
        },
        bottomBar = {
            SessionControlsBar(
                state = sessionState,
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
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White.copy(alpha = backgroundColor))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .onRotaryScrollEvent {
                        if (it.verticalScrollPixels > 0) {
                            mainViewModel.incInterval()
                        } else {
                            mainViewModel.decInterval()
                        }
                        true
                    }
                    .focusRequester(focusRequester)
                    .focusable(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Interval: $interval s",
                    style = MaterialTheme.typography.title2,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { mainViewModel.decInterval() }) {
                        Text(text = "-0.5s")
                    }
                    Button(onClick = { mainViewModel.incInterval() }) {
                        Text(text = "+0.5s")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    state = lazyListState,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listOf(0.5, 1.0, 1.5, 2.0, 2.5, 3.0)) { preset ->
                        Chip(onClick = { mainViewModel.setInterval(preset) }, label = { Text(text = "$preset") })
                    }
                }
                coroutineScope.launch {
                    lazyListState.scrollBy(0f)
                }
            }
        }
    }
}
