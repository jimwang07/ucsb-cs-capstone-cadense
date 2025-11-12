package com.example.stride.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.example.stride.presentation.theme.Black
import com.example.stride.presentation.theme.EmeraldDark
import com.example.stride.presentation.theme.EmeraldGreen
import com.example.stride.presentation.theme.White
import com.example.stride.presentation.viewmodel.SettingsViewModel
import com.example.stride.haptics.HapticsController

private val GrayTrack = Color(0xFF2D2D2D)

@Composable
fun AdjustModesScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val isVisualEnabled by viewModel.isVisualEnabled.collectAsState()
    val isAudioEnabled by viewModel.isAudioEnabled.collectAsState()
    val isVibrationEnabled by viewModel.isVibrationEnabled.collectAsState()

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
                .offset(x = (-8).dp)
                .size(backButtonTouchTarget)
                .clip(CircleShape)
                .clickable(
                    interactionSource = backInteractionSource,
                    indication = null,
                    onClick = onBack
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Back",
                tint = backButtonColor,
                modifier = Modifier.size(backButtonIconSize)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 260.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                ModeRow(
                    label = "Visual",
                    checked = isVisualEnabled,
                    onToggle = { viewModel.setVisualEnabled(it) }
                )
                ModeRow(
                    label = "Audio",
                    checked = isAudioEnabled,
                    onToggle = { viewModel.setAudioEnabled(it) }
                )
                ModeRow(
                    label = "Vibrate",
                    checked = isVibrationEnabled,
                    onToggle = { viewModel.setVibrationEnabled(it) }
                )
            }
        }
    }
}

@Composable
private fun ModeRow(
    label: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        )
        CompactSwitch(
            checked = checked,
            onCheckedChange = onToggle
        )
    }
}

@Composable
private fun CompactSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val hapticsController = remember { HapticsController(context) }

    DisposableEffect(Unit) {
        onDispose {
            hapticsController.cancel()
        }
    }

    val trackWidth = 42.dp
    val trackHeight = 24.dp
    val thumbDiameter = 20.dp
    val padding = (trackHeight - thumbDiameter) / 2

    val trackColor by animateColorAsState(
        targetValue = if (checked) EmeraldGreen else GrayTrack,
        animationSpec = tween(150)
    )

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) (trackWidth - thumbDiameter - padding) else padding,
        animationSpec = tween(150)
    )

    Box(
        modifier = Modifier
            .width(trackWidth)
            .height(trackHeight)
            .clip(CircleShape)
            .background(trackColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                hapticsController.vibrate(20)
                onCheckedChange(!checked)
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(thumbDiameter)
                .clip(CircleShape)
                .background(White)
        )
    }
}
