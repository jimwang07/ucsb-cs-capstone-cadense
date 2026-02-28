package com.example.stride.presentation

import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.stride.navigation.WearAppNavHost
import com.example.stride.presentation.theme.TestLockScreenTheme
import com.example.stride.presentation.viewmodel.MetronomeViewModel
import com.example.stride.sensors.ManualStrikeTrigger

class MainActivity : ComponentActivity() {

    private val metronomeViewModel: MetronomeViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep the screen on throughout the entire app
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            WearApp(metronomeViewModel)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_SPACE) {
            ManualStrikeTrigger.trigger()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}

@Composable
fun WearApp(metronomeViewModel: MetronomeViewModel) {
    TestLockScreenTheme {
        val navController = rememberSwipeDismissableNavController()
        WearAppNavHost(navController = navController, metronomeViewModel = metronomeViewModel)
    }
}
