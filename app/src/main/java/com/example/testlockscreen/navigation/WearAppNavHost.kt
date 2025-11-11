package com.example.testlockscreen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import com.example.testlockscreen.presentation.ui.AdjustMetronomeScreen
import com.example.testlockscreen.presentation.ui.AdjustModesScreen
import com.example.testlockscreen.presentation.ui.LandingScreen
import com.example.testlockscreen.presentation.ui.SessionScreen
import com.example.testlockscreen.presentation.ui.SettingsScreen
import com.example.testlockscreen.presentation.viewmodel.MetronomeViewModel
import com.example.testlockscreen.presentation.viewmodel.SettingsViewModel

@Composable
fun WearAppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    metronomeViewModel: MetronomeViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel()
) {
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Screen.Landing.route,
        modifier = modifier
    ) {
        composable(Screen.Landing.route) {
            LandingScreen(
                onStartSession = { navController.navigate(Screen.Session.route) },
                onShowSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onAdjustMetronome = { navController.navigate(Screen.AdjustMetronome.route) },
                onAdjustModes = { navController.navigate(Screen.AdjustModes.route) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.AdjustMetronome.route) {
            AdjustMetronomeScreen(
                viewModel = settingsViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.AdjustModes.route) {
            AdjustModesScreen(
                viewModel = settingsViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Session.route) {
            SessionScreen(
                metronomeViewModel = metronomeViewModel,
                settingsViewModel = settingsViewModel,
                onEndSession = { time, distance, poleStrikes ->
                    // TODO: Implement navigation to a Session Summary screen
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
