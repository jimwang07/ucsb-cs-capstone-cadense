package com.example.stride.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.QueryBuilder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text

private val EmeraldGreen = Color(0xFF34D399)
private val EmeraldPressed = Color(0xFF10B981)
private val BlueIcon = Color(0xFF60A5FA)
private val BlueBg = Color(0x333B82F6)
private val PurpleIcon = Color(0xFFC084FC)
private val PurpleBg = Color(0x33A855F7)
private val AmberIcon = Color(0xFFFBBF24)
private val AmberBg = Color(0x33F59E0B)
private val GrayText = Color(0xFF9CA3AF)
private val White = Color(0xFFFFFFFF)
private val Black = Color(0xFF000000)

@Composable
fun SessionCompleteScreen(
    sessionData: SessionData,
    onDoneClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = screenHeight * 0.04f, horizontal = screenWidth * 0.03f),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SuccessHeader()
            StatisticsGrid(sessionData)
            DoneButton(onDoneClick)
        }
    }
}

@Composable
private fun SuccessHeader() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(screenWidth * 0.12f) // Smaller checkmark circle
                .clip(CircleShape)
                .background(EmeraldGreen),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Success",
                modifier = Modifier.size(screenWidth * 0.06f), // Smaller checkmark icon
                tint = Black
            )
        }
        Spacer(modifier = Modifier.height(screenHeight * 0.01f)) // 1% of screen height
        Text(
            text = "Session Complete",
            color = EmeraldGreen,
            fontSize = (screenHeight.value * 0.055f).sp, // ~5.5% of screen height
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StatisticsGrid(sessionData: SessionData) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier.fillMaxWidth(0.8f), // 80% of screen width
        verticalArrangement = Arrangement.spacedBy(screenHeight * 0.0205f) // 2.05% of screen height
    ) {
        StatRow(
            icon = Icons.Default.QueryBuilder,
            iconBgColor = BlueBg,
            iconColor = BlueIcon,
            label = "Time",
            value = formatTime(sessionData.time),
            unit = ""
        )
        StatRow(
            icon = Icons.Default.Navigation,
            iconBgColor = PurpleBg,
            iconColor = PurpleIcon,
            label = "Distance",
            value = sessionData.distance.toString(),
            unit = "m"
        )
        StatRow(
            icon = Icons.Default.Bolt,
            iconBgColor = AmberBg,
            iconColor = AmberIcon,
            label = "Strikes",
            value = sessionData.poleStrikes.toString(),
            unit = ""
        )
    }
}

@Composable
private fun StatRow(
    icon: ImageVector,
    iconBgColor: Color,
    iconColor: Color,
    label: String,
    value: String,
    unit: String
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(screenWidth * 0.103f) // 10.3% of screen width
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(screenWidth * 0.051f), // 5.1% of screen width
                    tint = iconColor
                )
            }
            Spacer(modifier = Modifier.width(screenWidth * 0.0205f)) // 2.05% of screen width
            Text(
                text = label,
                color = GrayText,
                fontSize = (screenHeight.value * 0.05f).sp // 5% of screen height
            )
        }
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                color = White,
                fontSize = (screenHeight.value * 0.12f).sp,
                fontFamily = FontFamily.Monospace
            )
            if (unit.isNotEmpty()) {
                Text(
                    text = unit,
                    color = GrayText,
                    fontSize = (screenHeight.value * 0.054f).sp, // 5.4% of screen height
                    modifier = Modifier.padding(bottom = (screenHeight * 0.01f))
                )
            }
        }
    }
}

@Composable
private fun DoneButton(onDone: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundColor by animateColorAsState(if (isPressed) EmeraldPressed else EmeraldGreen, label = "background")

    Box(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(50)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(50)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Disable ripple to show color change
                onClick = onDone
            )
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Done",
            color = Black,
            fontWeight = FontWeight.Normal,
            fontSize = (LocalConfiguration.current.screenHeightDp * 0.05f).sp
        )
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}
