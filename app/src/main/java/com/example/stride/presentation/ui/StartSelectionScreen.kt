package com.example.stride.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

@Composable
fun StartSelectionScreen(
    onStandardSelected: () -> Unit,
    onCalibrationSelected: () -> Unit
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
        Text(
            text = "Select Mode",
            fontSize = (screenHeight.value * 0.08f).sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(screenHeight * 0.05f))

        Button(
            onClick = onStandardSelected,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.25f)
        ) {
            Text(
                text = "Standard Metronome",
                fontSize = (screenHeight.value * 0.07f).sp
            )
        }

        Spacer(modifier = Modifier.height(screenHeight * 0.04f))

        Button(
            onClick = onCalibrationSelected,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.DarkGray,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.25f)
        ) {
            Text(
                text = "Calibration Mode",
                fontSize = (screenHeight.value * 0.07f).sp
            )
        }
    }
}
