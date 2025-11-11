package com.example.testlockscreen.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import com.example.testlockscreen.presentation.theme.Black
import com.example.testlockscreen.presentation.theme.EmeraldDark
import com.example.testlockscreen.presentation.theme.EmeraldGreen
import com.example.testlockscreen.presentation.viewmodel.SettingsViewModel

@Composable
fun AdjustMetronomeScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val bpm by viewModel.defaultBpm.collectAsState()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val backButtonIconSize = screenHeight * 0.12f
    val backButtonTouchTarget = screenHeight * 0.15f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(all = screenHeight * 0.02f)
    ) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = { viewModel.setDefaultBpm(bpm - 1) }) {
                        Text(text = "-")
                    }
                    Text(text = "$bpm")
                    Button(onClick = { viewModel.setDefaultBpm(bpm + 1) }) {
                        Text(text = "+")
                    }
                }
            }
        }

        val backInteractionSource = remember { MutableInteractionSource() }
        val isBackPressed by backInteractionSource.collectIsPressedAsState()
        val backButtonColor by animateColorAsState(
            targetValue = if (isBackPressed) EmeraldDark else EmeraldGreen,
            animationSpec = tween(durationMillis = 200),
            label = "BackButtonColor"
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(backButtonTouchTarget)
                .clip(CircleShape)
                .clickable(interactionSource = backInteractionSource, indication = null, onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Back",
                modifier = Modifier.size(backButtonIconSize),
                tint = backButtonColor
            )
        }
    }
}
