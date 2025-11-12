package com.example.stride.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.stride.navigation.WearAppNavHost
import com.example.stride.presentation.theme.TestLockScreenTheme
import com.example.stride.presentation.viewmodel.MetronomeViewModel

class MainActivity : ComponentActivity() {

    private val metronomeViewModel: MetronomeViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp(metronomeViewModel)
        }
    }
}

@Composable
fun WearApp(metronomeViewModel: MetronomeViewModel) {
    TestLockScreenTheme {
        val navController = rememberSwipeDismissableNavController()
        WearAppNavHost(navController = navController, metronomeViewModel = metronomeViewModel)
    }
}