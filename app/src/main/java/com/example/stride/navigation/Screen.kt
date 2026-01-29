package com.example.stride.navigation

sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object Settings : Screen("settings")
    object AdjustMetronome : Screen("adjust_metronome")
    object TapBpm : Screen("tap_bpm")
    object AdjustModes : Screen("adjust_modes")
    object Session : Screen("session")
    object SessionComplete : Screen("session_complete/{time}/{poleStrikes}/{onBeatPercent}/{avgOffset}") {
        fun createRoute(
            time: Int,
            poleStrikes: Int,
            onBeatPercent: Int,
            avgOffset: Int
        ) = "session_complete/$time/$poleStrikes/$onBeatPercent/$avgOffset"
    }
    object PastSessions : Screen("past_sessions")
}
