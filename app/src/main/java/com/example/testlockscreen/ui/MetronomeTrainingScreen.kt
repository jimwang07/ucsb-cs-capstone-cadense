package com.example.testlockscreen.ui

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.example.testlockscreen.haptics.HapticsController
import com.example.testlockscreen.navigation.Screen
import com.example.testlockscreen.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun MetronomeTrainingScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = viewModel()
) {
    val bpm by mainViewModel.bpm.collectAsState()
    val sessionState by mainViewModel.sessionState.collectAsState()
    val isRecording by mainViewModel.isRecording.collectAsState()
    val connectionState by mainViewModel.connectionState.collectAsState()

    val hapticsController = HapticsController(LocalContext.current)
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        onDispose {
            hapticsController.cancelHaptics()
        }
    }

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
                onStart = {
                    mainViewModel.startSession()
                    hapticsController.playMetronomeBeat(coroutineScope, bpm)
                },
                onStop = {
                    mainViewModel.stopSession()
                    hapticsController.cancelHaptics()
                },
                onEnd = {
                    mainViewModel.endSession()
                    hapticsController.cancelHaptics()
                    navController.navigate(Screen.EndSession.route) {
                        popUpTo(Screen.Landing.route) { inclusive = true }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .onRotaryScrollEvent {
                    if (it.verticalScrollPixels > 0) {
                        mainViewModel.incBpm()
                    } else {
                        mainViewModel.decBpm()
                    }
                    true
                }
                .focusRequester(focusRequester)
                .focusable(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$bpm BPM",
                style = MaterialTheme.typography.display1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { mainViewModel.decBpm() }) {
                    Text(text = "-1")
                }
                Button(onClick = { mainViewModel.incBpm() }) {
                    Text(text = "+1")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                state = lazyListState,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listOf(40, 60, 80, 100, 120)) { preset ->
                    Chip(onClick = { mainViewModel.setBpm(preset) }, label = { Text(text = preset.toString()) })
                }
            }
            coroutineScope.launch {
                lazyListState.scrollBy(0f)
            }
        }
    }
}
