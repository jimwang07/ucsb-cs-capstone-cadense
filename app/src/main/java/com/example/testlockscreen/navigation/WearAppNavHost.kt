package com.example.testlockscreen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import com.example.testlockscreen.ui.BasicSettingsScreen
import com.example.testlockscreen.ui.EndSessionScreen
import com.example.testlockscreen.ui.LandingPage
import com.example.testlockscreen.ui.MetronomeTrainingScreen
import com.example.testlockscreen.ui.ModeSelectionScreen
import com.example.testlockscreen.ui.VisualCueTrainingScreen
import com.example.testlockscreen.viewmodel.MainViewModel

@Composable
fun WearAppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel()
) {
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Screen.Landing.route,
        modifier = modifier
    ) {
        composable(Screen.Landing.route) {
            LandingPage(
                onStartClick = { navController.navigate(Screen.ModeSelection.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.ModeSelection.route) {
            ModeSelectionScreen(navController = navController)
        }
        composable(Screen.MetronomeTraining.route) {
            MetronomeTrainingScreen(navController = navController, mainViewModel = mainViewModel)
        }
        composable(Screen.VisualCueTraining.route) {
            VisualCueTrainingScreen(navController = navController, mainViewModel = mainViewModel)
        }
        composable(Screen.EndSession.route) {
            EndSessionScreen(navController = navController, mainViewModel = mainViewModel)
        }
        composable(Screen.Settings.route) {
            BasicSettingsScreen(onDone = { navController.popBackStack() })
        }
    }
}
