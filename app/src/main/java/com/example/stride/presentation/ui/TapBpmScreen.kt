package com.example.stride.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleButton
import androidx.wear.compose.material.ToggleButtonDefaults
import com.example.stride.haptics.HapticsController
import com.example.stride.presentation.theme.EmeraldDark
import com.example.stride.presentation.theme.EmeraldGreen
import com.example.stride.presentation.viewmodel.TapBpmViewModel

// Color Palette
private val EmeraldLight = Color(0xFF6EE7B7)
private val EmeraldDim = Color(0xFF059669)
private val Black = Color(0xFF000000)
private val White = Color(0xFFFFFFFF)
private val Emerald100 = Color(0xFFD1FAE5)

@Composable
fun TapBpmScreen(
    onBpmChange: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: TapBpmViewModel = viewModel()
) {
    val context = LocalContext.current
    val hapticsController = remember { HapticsController(context) }
    val tapTimestamps by viewModel.tapTimestamps.collectAsState()
    val isAutoDetectEnabled by viewModel.isAutoDetectEnabled.collectAsState()

    val flashScale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        viewModel.strikeDetectedEvent.collect {
            hapticsController.vibrate(15)
            flashScale.snapTo(1.2f)
            flashScale.animateTo(1f, tween(100))
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            hapticsController.cancel()
        }
    }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val currentBpm = viewModel.getCalculatedBpm()

    LaunchedEffect(currentBpm) {
        if (currentBpm > 0) {
            onBpmChange(currentBpm)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(all = screenHeight * 0.05f)
    ) {
        // Back Button
        val backInteractionSource = remember { MutableInteractionSource() }
        val isBackPressed by backInteractionSource.collectIsPressedAsState()
        val backButtonColor by animateColorAsState(
            targetValue = if (isBackPressed) EmeraldLight else EmeraldGreen,
            label = "BackButtonColor"
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-12).dp)
                .size(screenHeight * 0.15f)
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
                modifier = Modifier.size(screenHeight * 0.12f),
                tint = backButtonColor
            )
        }

        // Reset Button
        val resetInteractionSource = remember { MutableInteractionSource() }
        val isResetPressed by resetInteractionSource.collectIsPressedAsState()

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(screenHeight * 0.15f)
                .clip(CircleShape)
                .clickable(
                    interactionSource = resetInteractionSource,
                    indication = null,
                    onClick = {
                        hapticsController.vibrate(20)
                        viewModel.reset()
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Reset",
                modifier = Modifier.size(screenHeight * 0.10f),
                tint = if (isResetPressed) EmeraldLight else EmeraldGreen
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isAutoDetectEnabled) "AUTO DETECT" else "TAP BPM",
                color = if (isAutoDetectEnabled) EmeraldGreen else White.copy(alpha = 0.6f),
                fontSize = (screenHeight * 0.035f).value.sp,
                letterSpacing = 0.1.em,
                textAlign = TextAlign.Center
            )

            Text(
                text = if (currentBpm > 0) currentBpm.toString() else "--",
                style = TextStyle(
                    brush = Brush.verticalGradient(
                        colors = listOf(White, Emerald100)
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = (screenHeight * 0.16f).value.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.scale(flashScale.value)
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.02f))

            // Large Tap Button
            val tapInteractionSource = remember { MutableInteractionSource() }
            val isTapPressed by tapInteractionSource.collectIsPressedAsState()

            val tapButtonBg = if (isTapPressed) {
                Brush.verticalGradient(listOf(EmeraldLight, EmeraldDim))
            } else {
                Brush.verticalGradient(listOf(EmeraldDark, EmeraldDim))
            }

            Box(
                modifier = Modifier
                    .size(screenWidth * 0.4f)
                    .shadow(
                        elevation = if (isTapPressed) 8.dp else 4.dp,
                        shape = CircleShape,
                        spotColor = EmeraldDark.copy(alpha = 0.4f)
                    )
                    .background(tapButtonBg, CircleShape)
                    .border(1.dp, EmeraldGreen, CircleShape)
                    .clickable(
                        interactionSource = tapInteractionSource,
                        indication = null,
                        onClick = {
                            hapticsController.vibrate(15)
                            viewModel.onManualTap()
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TAP",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = (screenHeight * 0.06f).value.sp
                )
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.04f))

            // Auto-Detect Toggle
            ToggleButton(
                checked = isAutoDetectEnabled,
                onCheckedChange = { viewModel.toggleAutoDetect() },
                modifier = Modifier.size(screenHeight * 0.15f),
                colors = ToggleButtonDefaults.toggleButtonColors(
                    checkedBackgroundColor = EmeraldGreen,
                    uncheckedBackgroundColor = Color.Gray.copy(alpha = 0.3f),
                    checkedContentColor = Black,
                    uncheckedContentColor = White
                )
            ) {
                Text(
                    text = "IMU",
                    fontSize = (screenHeight * 0.04f).value.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
