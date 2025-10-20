package com.example.testlockscreen.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.testlockscreen.navigation.Screen
import com.example.testlockscreen.viewmodel.MainViewModel

@Composable
fun EndSessionScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    val sessionSummary by mainViewModel.sessionSummary.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Session Summary",
            style = MaterialTheme.typography.title1,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Placeholder for session data
        Text(text = "Elapsed Time: ${sessionSummary.elapsedTime}")
        Text(text = "Mode: ${sessionSummary.mode}")
        Text(text = "Beats/Flips: ${sessionSummary.count}")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                mainViewModel.saveSession()
                Toast.makeText(context, "Saved locally", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.Landing.route) {
                    popUpTo(Screen.Landing.route) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save and Finish")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                mainViewModel.discardSession()
                navController.navigate(Screen.Landing.route) {
                    popUpTo(Screen.Landing.route) { inclusive = true }
                }
            },
            colors = ButtonDefaults.secondaryButtonColors(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Discard")
        }
    }
}
