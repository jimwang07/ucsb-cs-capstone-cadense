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
import androidx.compose.ui.platform.LocalConfiguration
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
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = screenWidth * 0.08f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1.2f)) // Pushes content down

        // Logo
        Image(
            painter = painterResource(id = R.drawable.cadense_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(screenHeight * 0.18f)
        )

        Spacer(modifier = Modifier.height(screenHeight * 0.02f))

        // Start Session Button
        Button(
            onClick = onStartSession,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.2f) // Set explicit height
        ) {
            Text(text = "Start Session", fontSize = (screenHeight.value * 0.08f).sp)
        }

        Spacer(modifier = Modifier.height(screenHeight * 0.04f))

        // Settings Button
        Button(
            onClick = onShowSettings,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.DarkGray,
                contentColor = Color.White
            ),
            modifier = Modifier.size(width = screenWidth * 0.65f, height = screenHeight * 0.15f)
        ) {
            Text(text = "Settings", fontSize = (screenHeight.value * 0.07f).sp)
        }

        Spacer(modifier = Modifier.height(screenHeight * 0.04f))

        // Past Sessions Button
        Button(
            onClick = { /* No action */ },
            enabled = false,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                disabledBackgroundColor = Color.DarkGray,
                disabledContentColor = Color.White
            ),
            modifier = Modifier.size(width = screenWidth * 0.65f, height = screenHeight * 0.15f)
        ) {
            Text(text = "Past Sessions", fontSize = (screenHeight.value * 0.07f).sp)
        }
        Spacer(modifier = Modifier.weight(1.3f))
    }
}
