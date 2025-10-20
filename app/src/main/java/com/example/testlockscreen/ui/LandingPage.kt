package com.example.testlockscreen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.testlockscreen.R

@Composable
fun LandingPage(
    onStartClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.display1
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onStartClick,
            modifier = Modifier.size(ButtonDefaults.LargeButtonSize)
        ) {
            Text(text = "Start")
        }

        Spacer(modifier = Modifier.height(12.dp))

//        Button(
//            onClick = onSettingsClick,
//            modifier = Modifier.size(ButtonDefaults.SmallButtonSize),
//            colors = ButtonDefaults.secondaryButtonColors()
//        ) {
//            Text(text = "Settings")
//        }
    }
}
