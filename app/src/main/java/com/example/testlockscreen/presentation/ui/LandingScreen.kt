package com.example.testlockscreen.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.testlockscreen.R

@Composable
fun LandingScreen(
    onStartSession: () -> Unit,
    onShowSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1.2f)) // Pushes content down

        // Logo
        Image(
            painter = painterResource(id = R.drawable.cadense_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(36.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Start Session Button
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

        Spacer(modifier = Modifier.height(8.dp))

        // Settings Button
        Button(
            onClick = onShowSettings,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.DarkGray,
                contentColor = Color.White
            ),
            modifier = Modifier.size(width = 150.dp, height = 30.dp)
        ) {
            Text(text = "Settings", fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Past Sessions Button
        Button(
            onClick = { /* No action */ },
            enabled = false,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                disabledBackgroundColor = Color.DarkGray,
                disabledContentColor = Color.White
            ),
            modifier = Modifier.size(width = 150.dp, height = 30.dp)
        ) {
            Text(text = "Past Sessions", fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.weight(1.3f))
    }
}
