package com.example.testlockscreen.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import com.example.testlockscreen.R

@Composable
fun LandingScreen(
    onStartSession: () -> Unit,
    onShowSettings: () -> Unit
) {
    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.cadense_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(48.dp)
            )
        }
        item {
            Button(
                onClick = onStartSession,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Start Session")
            }
        }
        item {
            Button(
                onClick = onShowSettings,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.DarkGray,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Settings")
            }
        }
        item {
            Button(
                onClick = { /* No action */ },
                enabled = false,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    disabledBackgroundColor = Color.DarkGray,
                    disabledContentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Past Sessions")
            }
        }
    }
}
