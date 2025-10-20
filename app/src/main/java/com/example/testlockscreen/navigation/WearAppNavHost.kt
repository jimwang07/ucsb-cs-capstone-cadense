package com.example.testlockscreen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import com.example.testlockscreen.ui.BasicSettingsScreen
import com.example.testlockscreen.ui.EndSessionScreen
import com.example.testlockscreen.ui.LandingPage
import com.example.testlockscreen.ui.MetronomeTrainingScreen
import com.example.testlockscreen.ui.ModeSelectionScreen
import com.example.testlockscreen.ui.VisualCueTrainingScreen

@Composable
fun WearAppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Screen.Landing.route,
        modifier = modifier
    ) {
        composable(Screen.Landing.route) {
            LandingPage(navController = navController)
        }
        composable(Screen.ModeSelection.route) {
            ModeSelectionScreen(navController = navController)
        }
        composable(Screen.MetronomeTraining.route) {
            MetronomeTrainingScreen(navController = navController)
        }
        composable(Screen.VisualCueTraining.route) {
            VisualCueTrainingScreen(navController = navController)
        }
        composable(Screen.EndSession.route) {
            EndSessionScreen(navController = navController)
        }
        composable(Screen.Settings.route) {
            BasicSettingsScreen(navController = navController)
        }
    }
}
