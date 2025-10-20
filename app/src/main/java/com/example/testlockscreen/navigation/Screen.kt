package com.example.testlockscreen.navigation

sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object ModeSelection : Screen("mode_selection")
    object MetronomeTraining : Screen("metronome_training")
    object VisualCueTraining : Screen("visual_cue_training")
    object EndSession : Screen("end_session")
    object Settings : Screen("settings")
}
