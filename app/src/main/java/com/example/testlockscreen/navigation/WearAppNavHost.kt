package com.example.testlockscreen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import com.example.testlockscreen.presentation.ui.*
import com.example.testlockscreen.presentation.viewmodel.MetronomeViewModel

@Composable
fun WearAppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    metronomeViewModel: MetronomeViewModel = viewModel()
) {
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Screen.Landing.route,
        modifier = modifier
    ) {
        composable(Screen.Landing.route) {
            LandingScreen(
                onStartSession = { navController.navigate(Screen.StartSession.route) },
                onShowSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                initialBpm = metronomeViewModel.bpm.value,
                onBpmChange = { newBpm -> metronomeViewModel.setBpm(newBpm) }
            )
        }
        composable(Screen.StartSession.route) {
            StartSessionScreen(
                onVisualSession = { navController.navigate(Screen.VisualSession.route) },
                onAudioSession = { navController.navigate(Screen.AudioSession.route) },
                onVibrationSession = { navController.navigate(Screen.VibrationSession.route) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.VisualSession.route) {
            VisualSessionScreen(
                viewModel = metronomeViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.AudioSession.route) {
            AudioSessionScreen(
                viewModel = metronomeViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.VibrationSession.route) {
            VibrationSessionScreen(
                viewModel = metronomeViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
