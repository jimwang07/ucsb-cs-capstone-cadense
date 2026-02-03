package com.example.stride.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import com.example.stride.data.AppDatabase
import com.example.stride.presentation.ui.AdjustMetronomeScreen
import com.example.stride.presentation.ui.AdjustModesScreen
import com.example.stride.presentation.ui.CalibrationScreen
import com.example.stride.presentation.ui.LandingScreen
import com.example.stride.presentation.ui.PastSessionsScreen
import com.example.stride.presentation.ui.SessionCompleteScreen
import com.example.stride.presentation.ui.SessionData
import com.example.stride.presentation.ui.SessionScreen
import com.example.stride.presentation.ui.SettingsScreen
import com.example.stride.presentation.ui.StartSelectionScreen
import com.example.stride.presentation.ui.TapBpmScreen
import com.example.stride.presentation.viewmodel.MetronomeViewModel
import com.example.stride.presentation.viewmodel.PastSessionsViewModel
import com.example.stride.presentation.viewmodel.PastSessionsViewModelFactory
import com.example.stride.presentation.viewmodel.SettingsViewModel
import com.example.stride.timing.TimingStats

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
            val stopwatch by metronomeViewModel.stopwatch.collectAsState()
            val currentBpm by metronomeViewModel.bpm.collectAsState()
            LandingScreen(
                onStartSession = {
                    if (stopwatch > 0) {
                        navController.navigate(Screen.Session.createRoute(currentBpm))
                    } else {
                        navController.navigate(Screen.StartSelection.route)
                    }
                },
                onShowSettings = { navController.navigate(Screen.Settings.route) },
                onShowPastSessions = { navController.navigate(Screen.PastSessions.route) },
                isSessionInProgress = stopwatch > 0
            )
        }

        composable(Screen.StartSelection.route) {
            val defaultBpm by settingsViewModel.defaultBpm.collectAsState()
            StartSelectionScreen(
                onStandardSelected = {
                    navController.navigate(Screen.Session.createRoute(defaultBpm))
                },
                onCalibrationSelected = {
                    navController.navigate(Screen.Calibration.route)
                }
            )
        }

        composable(Screen.Calibration.route) {
            CalibrationScreen(
                onCalibrationComplete = { bpm ->
                    navController.navigate(Screen.Session.createRoute(bpm)) {
                        // Pop calibration screen from backstack so back goes to landing/selection
                        popUpTo(Screen.StartSelection.route) { inclusive = true }
                    }
                }
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
                onTapBpm = { navController.navigate(Screen.TapBpm.route) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.TapBpm.route) {
            TapBpmScreen(
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
        composable(
            route = Screen.Session.route,
            arguments = listOf(navArgument("bpm") { type = NavType.IntType })
        ) { backStackEntry ->
            val bpm = backStackEntry.arguments?.getInt("bpm") ?: 60
            SessionScreen(
                metronomeViewModel = metronomeViewModel,
                settingsViewModel = settingsViewModel,
                startingBpm = bpm,
                onEndSession = { time, poleStrikes, timingStats ->
                    navController.navigate(
                        Screen.SessionComplete.createRoute(
                            time = time,
                            poleStrikes = poleStrikes,
                            onBeatPercent = timingStats.onBeatPercentage.toInt(),
                            avgOffset = timingStats.averageOffsetMs.toInt()
                        )
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.SessionComplete.route,
            arguments = listOf(
                navArgument("time") { type = NavType.IntType },
                navArgument("poleStrikes") { type = NavType.IntType },
                navArgument("onBeatPercent") { type = NavType.IntType },
                navArgument("avgOffset") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val time = backStackEntry.arguments?.getInt("time") ?: 0
            val poleStrikes = backStackEntry.arguments?.getInt("poleStrikes") ?: 0
            val onBeatPercent = backStackEntry.arguments?.getInt("onBeatPercent") ?: 0
            val avgOffset = backStackEntry.arguments?.getInt("avgOffset") ?: 0
            SessionCompleteScreen(
                sessionData = SessionData(
                    time = time,
                    poleStrikes = poleStrikes,
                    timingStats = TimingStats(
                        totalStrikes = poleStrikes,
                        onBeatStrikes = (poleStrikes * onBeatPercent / 100),
                        offBeatStrikes = poleStrikes - (poleStrikes * onBeatPercent / 100),
                        totalAbsoluteOffsetMs = (avgOffset * poleStrikes).toLong()
                    )
                ),
                onDoneClick = {
                    navController.popBackStack(Screen.Landing.route, false)
                }
            )
        }

        // ---- PastSessions route ----
        composable(Screen.PastSessions.route) {
            val context = LocalContext.current

            val db = AppDatabase.getDatabase(context.applicationContext)
            val sessionDao = db.sessionDao()

            val pastSessionsViewModel: PastSessionsViewModel = viewModel(
                factory = PastSessionsViewModelFactory(sessionDao)
            )

            val sessions by pastSessionsViewModel.sessions.collectAsState()

            PastSessionsScreen(
                sessions = sessions,
                onBack = { navController.popBackStack() },
                onSelectSession = { index ->
                    val selected = sessions.getOrNull(index)
                    if (selected != null) {
                        navController.navigate(
                            Screen.SessionComplete.createRoute(
                                time = selected.time,
                                poleStrikes = selected.poleStrikes,
                                onBeatPercent = selected.timingStats.onBeatPercentage.toInt(),
                                avgOffset = selected.timingStats.averageOffsetMs.toInt()
                            )
                        )
                    }
                }
            )
        }
    }
}
