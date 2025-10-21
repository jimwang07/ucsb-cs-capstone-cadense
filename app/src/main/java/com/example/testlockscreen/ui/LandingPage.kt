package com.example.testlockscreen.ui

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.items
import androidx.wear.compose.material.rememberScalingLazyListState
import androidx.wear.compose.material.AutoCenteringParams
import com.example.testlockscreen.R

@Composable
fun LandingPage(
    onStartClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onPastSessionsClick: () -> Unit
) {
    val listState = rememberScalingLazyListState()
    val actions = listOf(
        Triple("Start Session", onStartClick, true),
        Triple("Past Sessions", onPastSessionsClick, false),
        Triple("Settings", onSettingsClick, false)
    )

    ScalingLazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        autoCentering = AutoCenteringParams(itemIndex = 0)
    ) {
        item {
            ListHeader {
                Text(
                    text = stringResource(id = R.string.app_name),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.title1,
                    color = MaterialTheme.colors.primary
                )
            }
        }
        items(actions) { (label, onClick, isPrimary) ->
            Chip(
                onClick = onClick,
                label = {
                    Text(text = label, textAlign = TextAlign.Center)
                },
                colors = if (isPrimary) {
                    ChipDefaults.primaryChipColors()
                } else {
                    ChipDefaults.secondaryChipColors()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
