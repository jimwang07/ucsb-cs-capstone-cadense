package com.example.testlockscreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

@Composable
fun StatusIndicatorChip(
    isRecording: Boolean,
    connectionState: String
) {
    Row(
        modifier = Modifier.height(28.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (isRecording) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color = MaterialTheme.colors.error, shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = connectionState,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.primary
        )
    }
}
