package com.example.testlockscreen.presentation

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.activity.OnBackPressedCallback

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Proper back press override
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing — block back gesture
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        // 🔒 Enter kiosk mode
        val dpm = getSystemService(DevicePolicyManager::class.java)
        val component = ComponentName(this, MyDeviceAdminReceiver::class.java)

// Only try to set lock task packages if DevicePolicyManager is available
        try {
            dpm.setLockTaskPackages(component, arrayOf(packageName))
            startLockTask() // may throw SecurityException on emulator
        } catch (e: SecurityException) {
            // Emulator doesn’t allow device-owner lock task → ignore
            e.printStackTrace()
        }

        // 🧭 Compose UI
        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Hello World",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.display1
        )
    }
}

// 🔐 Device admin receiver (required for lock task mode)
class MyDeviceAdminReceiver : android.app.admin.DeviceAdminReceiver()
