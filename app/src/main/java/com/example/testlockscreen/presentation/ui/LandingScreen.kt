package com.example.testlockscreen.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.example.testlockscreen.presentation.viewmodel.MetronomeViewModel

@Composable
fun LandingScreen(
    onStartSession: () -> Unit,
    onShowSettings: () -> Unit,
    metronomeViewModel: MetronomeViewModel
) {
    val context = LocalContext.current
    val permission = Manifest.permission.ACCESS_FINE_LOCATION

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        Log.d("LandingScreen", "Permission granted: $isGranted")
        if (isGranted) {
            metronomeViewModel.onLocationPermissionGranted()
            onStartSession()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "STRIDE",
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(
                onClick = {
                    Log.d("LandingScreen", "Start Session clicked")
                    val permissionStatus = ContextCompat.checkSelfPermission(context, permission)
                    Log.d("LandingScreen", "Permission status: $permissionStatus")
                    if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                        metronomeViewModel.onLocationPermissionGranted()
                        onStartSession()
                    } else {
                        Log.d("LandingScreen", "Launching permission request")
                        launcher.launch(permission)
                    }
                },
                modifier = Modifier.size(150.dp, 50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF29EAB6)
                )
            ) {
                Text(text = "Start Session", fontSize = 12.sp)
            }
        }
        Button(
            onClick = onShowSettings,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings"
            )
        }
    }
}
