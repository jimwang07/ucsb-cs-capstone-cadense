package com.example.stride.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text

// Colors
private val Black = Color(0xFF000000)
private val White = Color(0xFFFFFFFF)
private val EmeraldGreen = Color(0xFF34D399)
private val EmeraldDark = Color(0xFF059669)
private val CardBg = Color(0xFF111827)
private val CardBgPressed = Color(0xFF1F2937)
private val GrayText = Color(0xFF9CA3AF)

@Composable
fun PastSessionsScreen(
    sessions: List<SessionData>,
    onBack: () -> Unit,
    onSelectSession: (index: Int) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val backButtonIconSize = screenHeight * 0.12f
    val backButtonTouchTarget = screenHeight * 0.15f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(horizontal = screenHeight * 0.02f)
    ) {

        // 🔹 Back Button (now uses zIndex so it's clickable above everything)
        val backInteractionSource = remember { MutableInteractionSource() }
        val isBackPressed by backInteractionSource.collectIsPressedAsState()
        val backButtonColor by animateColorAsState(
            targetValue = if (isBackPressed) EmeraldDark else EmeraldGreen,
            animationSpec = tween(durationMillis = 200),
            label = "PastSessionsBackButtonColor"
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-8).dp)
                .size(backButtonTouchTarget)
                .zIndex(2f)  // 🟢 Ensures tap events go to the back button
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

        // 🔹 Title + Scrollable List
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(screenHeight * 0.08f)) // more top space

            Text(
                text = "Past Sessions",
                color = White,
                fontSize = (screenHeight.value * 0.08f).sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.03f))

            // 🔽 Scrollable content only below here
            if (sessions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No past sessions",
                        color = GrayText,
                        fontSize = (screenHeight.value * 0.06f).sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(screenHeight * 0.012f),
                    // Increased bottom padding to clear the circular screen curve
                    contentPadding = PaddingValues(bottom = screenHeight * 0.3f)
                ) {
                    itemsIndexed(sessions) { index, session ->
                        PastSessionCard(
                            index = index,
                            totalCount = sessions.size,
                            session = session,
                            screenHeight = screenHeight,
                            screenWidth = screenWidth,
                            onClick = { onSelectSession(index) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PastSessionCard(
    index: Int,
    totalCount: Int,
    session: SessionData,
    screenHeight: androidx.compose.ui.unit.Dp,
    screenWidth: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val bgColor by animateColorAsState(
        targetValue = if (isPressed) CardBgPressed else CardBg,
        animationSpec = tween(150),
        label = "PastSessionCardBg"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(bgColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(
                vertical = screenHeight * 0.015f,
                horizontal = screenWidth * 0.04f
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(screenHeight * 0.006f)
        ) {
            Text(
                text = "Session ${totalCount - index}",
                color = GrayText,
                fontSize = (screenHeight.value * 0.055f).sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(session.time),
                    color = White,
                    fontSize = (screenHeight.value * 0.07f).sp
                )
                Text(
                    text = "${session.poleStrikes} strikes",
                    color = White,
                    fontSize = (screenHeight.value * 0.055f).sp
                )
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}
