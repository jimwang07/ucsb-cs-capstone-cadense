package com.example.stride.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import com.example.stride.presentation.ui.AdjustMetronomeScreen
import com.example.stride.presentation.ui.AdjustModesScreen
import com.example.stride.presentation.ui.LandingScreen
import com.example.stride.presentation.ui.SessionScreen
import com.example.stride.presentation.ui.SettingsScreen
import com.example.stride.presentation.viewmodel.MetronomeViewModel
import com.example.stride.presentation.viewmodel.SettingsViewModel
import com.example.stride.presentation.ui.SessionCompleteScreen
import com.example.stride.presentation.ui.SessionData

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
                onShowSettings = { navController.navigate(Screen.Settings.route) },
                metronomeViewModel = metronomeViewModel
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
            val bpm by settingsViewModel.defaultBpm.collectAsState()
            AdjustMetronomeScreen(
                bpm = bpm,
                onBpmChange = { settingsViewModel.setDefaultBpm(it) },
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
                    navController.navigate(Screen.SessionComplete.createRoute(time, distance, poleStrikes))
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.SessionComplete.route,
            arguments = listOf(
                navArgument("time") { type = NavType.IntType },
                navArgument("distance") { type = NavType.IntType },
                navArgument("poleStrikes") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val time = backStackEntry.arguments?.getInt("time") ?: 0
            val distance = backStackEntry.arguments?.getInt("distance") ?: 0
            val poleStrikes = backStackEntry.arguments?.getInt("poleStrikes") ?: 0
            SessionCompleteScreen(
                sessionData = SessionData(time, distance, poleStrikes),
                onDoneClick = { navController.popBackStack(Screen.Landing.route, false) }
            )
        }
    }
}
