package com.example.testlockscreen.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.testlockscreen.navigation.WearAppNavHost
import com.example.testlockscreen.presentation.theme.TestLockScreenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    TestLockScreenTheme {
        val navController = rememberSwipeDismissableNavController()
        WearAppNavHost(navController = navController)
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}
