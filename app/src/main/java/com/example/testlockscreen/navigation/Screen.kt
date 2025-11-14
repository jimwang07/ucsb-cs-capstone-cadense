package com.example.testlockscreen.navigation

sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object Settings : Screen("settings")
    object AdjustMetronome : Screen("adjust_metronome")
    object AdjustModes : Screen("adjust_modes")
    object Session : Screen("session")
    object SessionComplete : Screen("session_complete/{time}/{distance}/{poleStrikes}") {
        fun createRoute(time: Int, distance: Int, poleStrikes: Int) = "session_complete/$time/$distance/$poleStrikes"
    }
}
