package com.example.testlockscreen.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.Text
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.testlockscreen.navigation.WearAppNavHost
import com.example.testlockscreen.presentation.theme.TestLockScreenTheme
import com.example.testlockscreen.presentation.viewmodel.MetronomeViewModel

class MainActivity : ComponentActivity() {

    private val metronomeViewModel: MetronomeViewModel by viewModels()

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