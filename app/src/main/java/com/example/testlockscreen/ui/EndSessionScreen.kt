package com.example.testlockscreen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.example.testlockscreen.R
import com.example.testlockscreen.navigation.Screen

@Composable
fun EndSessionScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.session_summary),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text("Elapsed Time: X min", modifier = Modifier.padding(bottom = 4.dp))
        Text("Beats/Cues: Y", modifier = Modifier.padding(bottom = 4.dp))
        Text("Avg BPM/Interval: Z", modifier = Modifier.padding(bottom = 16.dp))

        Button(
            onClick = {
                navController.navigate(Screen.Landing.route) {
                    popUpTo(Screen.Landing.route) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text(text = stringResource(R.string.save_and_finish))
        }

        Button(
            onClick = {
                navController.navigate(Screen.Landing.route) {
                    popUpTo(Screen.Landing.route) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.discard))
        }
    }
}
