package com.example.testlockscreen.navigation

sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object Settings : Screen("settings")
    object StartSession : Screen("start_session")
    object VisualSession : Screen("visual_session")
    object AudioSession : Screen("audio_session")
    object VibrationSession : Screen("vibration_session")
}
