package com.example.testlockscreen.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text

@Composable
fun StartSessionScreen(
    onVisualSession: () -> Unit,
    onAudioSession: () -> Unit,
    onVibrationSession: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onVisualSession,
            modifier = Modifier.size(120.dp, 40.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF29EAB6)
            )
        ) {
            Text(text = "Visual", fontSize = 15.sp)
        }
        Button(
            onClick = onAudioSession,
            modifier = Modifier.size(120.dp, 40.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF29EAB6)
            )
        ) {
            Text(text = "Audio", fontSize = 15.sp)
        }
        Button(
            onClick = onVibrationSession,
            modifier = Modifier.size(120.dp, 40.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF29EAB6)
            )
        ) {
            Text(text = "Vibration", fontSize = 15.sp)
        }
        Button(onClick = onBack, shape = RoundedCornerShape(12.dp)) {
            Text(text = "Back")
        }
    }
}
