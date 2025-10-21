package com.example.testlockscreen.ui

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.testlockscreen.navigation.Screen
import com.example.testlockscreen.viewmodel.MainViewModel

@Composable
fun EndSessionScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    val summary by mainViewModel.sessionSummary.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HomeChip(
            onHome = {
                navController.navigate(Screen.Landing.route) {
                    popUpTo(Screen.Landing.route) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Session Complete",
            style = MaterialTheme.typography.title1,
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Great work today.",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Duration: ${summary.duration}",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )
        Text(
            text = "Beats: ${summary.beats}",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )

        Chip(
            onClick = {
                navController.navigate(Screen.ModeSelection.route) {
                    popUpTo(Screen.Landing.route)
                }
            },
            label = { Text("Start Another Session") },
            colors = ChipDefaults.primaryChipColors()
        )
        Chip(
            onClick = {
                navController.navigate(Screen.Landing.route) {
                    popUpTo(Screen.Landing.route) { inclusive = true }
                }
            },
            label = { Text("Home") },
            colors = ChipDefaults.secondaryChipColors()
        )
    }
}
