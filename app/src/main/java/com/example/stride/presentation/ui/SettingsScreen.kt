package com.example.stride.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.example.stride.presentation.theme.Black
import com.example.stride.presentation.theme.EmeraldDark
import com.example.stride.presentation.theme.EmeraldGreen

@Composable
fun SettingsScreen(
    onAdjustMetronome: () -> Unit,
    onAdjustModes: () -> Unit,
    onBack: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val buttonWidth = screenWidth * 0.77f
    val buttonHeight = screenHeight * 0.25f
    val buttonTextSize = screenHeight * 0.08f
    val buttonGap = screenHeight * 0.06f
    val backButtonIconSize = screenHeight * 0.12f
    val backButtonTouchTarget = screenHeight * 0.15f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(all = screenHeight * 0.02f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .width(buttonWidth),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val adjustModeInteractionSource = remember { MutableInteractionSource() }
                val isAdjustModePressed by adjustModeInteractionSource.collectIsPressedAsState()
                val adjustModeColor by animateColorAsState(
                    targetValue = if (isAdjustModePressed) EmeraldDark else EmeraldGreen,
                    animationSpec = tween(durationMillis = 200),
                    label = "AdjustModeColor"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(buttonHeight)
                        .clip(RoundedCornerShape(buttonHeight / 2))
                        .background(adjustModeColor)
                        .clickable(interactionSource = adjustModeInteractionSource, indication = null, onClick = onAdjustModes),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Adjust Mode",
                        fontSize = buttonTextSize.value.sp,
                        fontWeight = FontWeight.Medium,
                        color = Black
                    )
                }

                Spacer(modifier = Modifier.height(buttonGap))

                val adjustMetronomeInteractionSource = remember { MutableInteractionSource() }
                val isAdjustMetronomePressed by adjustMetronomeInteractionSource.collectIsPressedAsState()
                val adjustMetronomeColor by animateColorAsState(
                    targetValue = if (isAdjustMetronomePressed) EmeraldDark else EmeraldGreen,
                    animationSpec = tween(durationMillis = 200),
                    label = "AdjustMetronomeColor"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(buttonHeight)
                        .clip(RoundedCornerShape(buttonHeight / 2))
                        .background(adjustMetronomeColor)
                        .clickable(interactionSource = adjustMetronomeInteractionSource, indication = null, onClick = onAdjustMetronome),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Adjust Metronome",
                        fontSize = buttonTextSize.value.sp,
                        fontWeight = FontWeight.Medium,
                        color = Black
                    )
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
                .offset(x = (-8).dp)
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