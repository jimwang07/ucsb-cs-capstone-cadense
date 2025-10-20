package com.example.testlockscreen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.example.testlockscreen.R
import com.example.testlockscreen.navigation.Screen

@Composable
fun LandingPage(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 20.dp)
        )

        Button(
            onClick = { navController.navigate(Screen.ModeSelection.route) },
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.primaryButtonColors()
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Start")
            Text(text = "Start")
        }

        Button(
            onClick = { navController.navigate(Screen.Settings.route) },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            colors = ButtonDefaults.secondaryButtonColors()
        ) {
            Icon(Icons.Default.Settings, contentDescription = "Settings")
            Text(text = "Settings")
        }
    }
    Vignette(vignettePosition = VignettePosition.TopAndBottom)
}
