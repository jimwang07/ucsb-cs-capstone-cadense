package com.example.testlockscreen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.testlockscreen.ui.HomeChip

@Composable
fun PastSessionsScreen(
    onHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
    ) {
        HomeChip(
            onHome = onHome,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Past Sessions",
            style = MaterialTheme.typography.title1,
            color = MaterialTheme.colors.primary
        )
        Text(
            text = "History coming soon.",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
