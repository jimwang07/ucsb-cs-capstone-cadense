package com.example.stride.navigation

sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object Settings : Screen("settings")
    object AdjustMetronome : Screen("adjust_metronome")
    object AdjustModes : Screen("adjust_modes")
    object Session : Screen("session")
}
