package com.example.testlockscreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.example.testlockscreen.R
import com.example.testlockscreen.haptics.HapticsController

@Composable
fun VisualCueTrainingScreen(
    navController: NavHostController
) {
    var visualCueInterval by remember { mutableStateOf(1.0f) }
    var isRecording by remember { mutableStateOf(false) }
    val hapticsEnabled by remember { mutableStateOf(true) }

    var currentBackgroundColor by remember { mutableStateOf(Color.Black) }

    val context = LocalContext.current
    val hapticsController = HapticsController(context)

    if (isRecording) {
        // Simplified alternation for now
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(currentBackgroundColor)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "%.1f s".format(visualCueInterval),
                color = if (currentBackgroundColor == Color.Black) Color.White else Color.Black,
                fontSize = 48.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { visualCueInterval = (visualCueInterval - 0.5f).coerceAtLeast(0.5f) }) {
                    Text("-")
                }
                Button(onClick = { visualCueInterval = 1.0f }) {
                    Text("1.0s")
                }
                Button(onClick = { visualCueInterval = (visualCueInterval + 0.5f).coerceAtMost(3.0f) }) {
                    Text("+")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Button(
                        onClick = {
                            isRecording = true
                            currentBackgroundColor = Color.Black // Reset to black on start
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
            )
        }
    }
}
