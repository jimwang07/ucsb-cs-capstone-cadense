package com.example.testlockscreen.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.Alignment
import androidx.wear.compose.material.AutoCenteringParams
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.items
import androidx.wear.compose.material.rememberScalingLazyListState
import com.example.testlockscreen.navigation.Screen
import com.example.testlockscreen.ui.HomeChip

private data class TrainingMode(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun ModeSelectionScreen(navController: NavHostController) {
    val modes = listOf(
        TrainingMode("Metronome Training", Icons.Default.MusicNote, Screen.MetronomeTraining.route),
        TrainingMode("Visual Cue Training", Icons.Default.Visibility, Screen.VisualCueTraining.route)
    )

    val listState = rememberScalingLazyListState()

    ScalingLazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        autoCentering = AutoCenteringParams(0)
    ) {
        item {
            HomeChip(
                onHome = {
                    navController.navigate(Screen.Landing.route) {
                        popUpTo(Screen.Landing.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Text(
                text = "Select Mode",
                style = MaterialTheme.typography.title1,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(modes) { mode ->
            Chip(
                onClick = { navController.navigate(mode.route) },
                label = { Text(text = mode.title) },
                icon = {
                    Icon(
                        imageVector = mode.icon,
                        contentDescription = mode.title
                    )
                },
                colors = ChipDefaults.primaryChipColors(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
