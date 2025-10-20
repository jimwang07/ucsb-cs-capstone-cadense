package com.example.testlockscreen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.example.testlockscreen.R
import com.example.testlockscreen.navigation.Screen

@Composable
fun SessionControlsOverlay(
    navController: NavHostController,
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = onStart) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Start Session")
                Text(text = stringResource(R.string.start))
            }
            Button(onClick = onStop) {
                Icon(Icons.Default.Stop, contentDescription = "Stop Session")
                Text(text = stringResource(R.string.stop))
            }
        }
        Button(
            onClick = {
                navController.navigate(Screen.EndSession.route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Cancel, contentDescription = "End Session")
            Text(text = stringResource(R.string.end_session))
        }
    }
}
