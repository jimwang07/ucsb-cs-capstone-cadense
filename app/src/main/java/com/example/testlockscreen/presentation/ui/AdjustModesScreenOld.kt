//package com.example.testlockscreen.presentation.ui
//
//import androidx.compose.animation.animateColorAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.interaction.collectIsPressedAsState
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.widthIn
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ChevronLeft
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.scale
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.wear.compose.material.Icon
//import androidx.wear.compose.material.Switch
//import androidx.wear.compose.material.SwitchDefaults
//import androidx.wear.compose.material.Text
//import com.example.testlockscreen.presentation.theme.Black
//import com.example.testlockscreen.presentation.theme.White
//import com.example.testlockscreen.presentation.viewmodel.SettingsViewModel
//
//private val EmeraldGreen = Color(0xFF34D399)
//private val EmeraldDark = Color(0xFF10B981) // For button press
//
//@Composable
//fun AdjustModesScreen(
//    viewModel: SettingsViewModel,
//    onBack: () -> Unit
//) {
//    val isVisualEnabled by viewModel.isVisualEnabled.collectAsState()
//    val isAudioEnabled by viewModel.isAudioEnabled.collectAsState()
//    val isVibrationEnabled by viewModel.isVibrationEnabled.collectAsState()
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Black)
//            .padding(16.dp) // p-4 from root div
//    ) {
//        // Back Button
//        val backInteractionSource = remember { MutableInteractionSource() }
//        val isBackPressed by backInteractionSource.collectIsPressedAsState()
//        val backButtonColor by animateColorAsState(
//            targetValue = if (isBackPressed) EmeraldDark else EmeraldGreen,
//            animationSpec = tween(durationMillis = 200),
//            label = "BackButtonColor"
//        )
//
//        Box(
//            modifier = Modifier
//                .align(Alignment.CenterStart)
//                // Touch target size
//                .size(48.dp)
//                .clip(CircleShape)
//                .clickable(
//                    interactionSource = backInteractionSource,
//                    indication = null,
//                    onClick = onBack
//                ),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = Icons.Default.ChevronLeft,
//                contentDescription = "Back",
//                // Icon size from ChevronLeft size={36}
//                modifier = Modifier.size(36.dp),
//                tint = backButtonColor
//            )
//        }
//
//        // Main content column - centers the toggle rows
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Column(
//                modifier = Modifier
//                    // max-w-[260px] from inner div
//                    .widthIn(max = 260.dp)
//                    .fillMaxWidth()
//                    // px-4 from inner div
//                    .padding(horizontal = 16.dp),
//                // gap-8 from inner div -> 2rem -> 32dp
//                verticalArrangement = Arrangement.spacedBy(32.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                // Visual Toggle
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text(
//                        text = "Visual",
//                        color = White,
//                        // text-3xl -> 1.875rem -> 30sp
//                        fontSize = 30.sp,
//                        fontWeight = FontWeight.Normal
//                    )
//                    Switch(
//                        checked = isVisualEnabled,
//                        onCheckedChange = { viewModel.setVisualEnabled(it) },
//                        modifier = Modifier.scale(2f),
//                        colors = SwitchDefaults.colors(
//                            checkedTrackColor = EmeraldGreen,
//                            checkedThumbColor = White,
//                            uncheckedThumbColor = White
//                        )
//                    )
//                }
//
//                // Audio Toggle
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text(
//                        text = "Audio",
//                        color = White,
//                        fontSize = 30.sp,
//                        fontWeight = FontWeight.Normal
//                    )
//                    Switch(
//                        checked = isAudioEnabled,
//                        onCheckedChange = { viewModel.setAudioEnabled(it) },
//                        modifier = Modifier.scale(2f),
//                        colors = SwitchDefaults.colors(
//                            checkedTrackColor = EmeraldGreen,
//                            checkedThumbColor = White,
//                            uncheckedThumbColor = White
//                        )
//                    )
//                }
//
//                // Vibration Toggle
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text(
//                        text = "Vibration",
//                        color = White,
//                        fontSize = 30.sp,
//                        fontWeight = FontWeight.Normal
//                    )
//                    Switch(
//                        checked = isVibrationEnabled,
//                        onCheckedChange = { viewModel.setVibrationEnabled(it) },
//                        modifier = Modifier.scale(2f),
//                        colors = SwitchDefaults.colors(
//                            checkedTrackColor = EmeraldGreen,
//                            checkedThumbColor = White,
//                            uncheckedThumbColor = White
//                        )
//                    )
//                }
//            }
//        }
//    }
//}