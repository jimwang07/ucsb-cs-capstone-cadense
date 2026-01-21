package com.example.stride.presentation.ui

import android.app.Activity
import android.util.Log
import android.view.WindowManager
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.example.stride.audio.AudioMetronome
import com.example.stride.haptics.HapticsController
import com.example.stride.presentation.viewmodel.MetronomeViewModel
import com.example.stride.presentation.viewmodel.SettingsViewModel
import com.example.stride.presentation.theme.EmeraldDark

import kotlin.math.roundToInt

// --- Color Palette ---
private val ColorWhite = Color(0xFFFFFFFF)
private val ColorMint = Color(0xFFA4F5DC)
private val ColorTeal = Color(0xFF29EAB6)
private val ColorUnfilled = Color(0xFF1F2937)
private val ColorUnfilledBorder = Color(0xFF374151)
private val ColorMutedGray = Color(0xFF6B7280)
private val ColorLightGray = Color(0xFF9CA3AF)
private val ColorButtonDarkGray = Color(0xFF374151)
private val ColorButtonRed = Color(0xFFDC2626)
private val ColorEmeraldGreen = Color(0xFF34D399)

@Composable
fun SessionScreen(
    metronomeViewModel: MetronomeViewModel,
    settingsViewModel: SettingsViewModel,
    onEndSession: (time: Int, distance: Int, poleStrikes: Int) -> Unit,
    onBack: () -> Unit
) {
    // --- State from ViewModels ---
    val isRunning by metronomeViewModel.isRunning.collectAsState()
    val beatCount by metronomeViewModel.beatCount.collectAsState()
    val stopwatch by metronomeViewModel.stopwatch.collectAsState()
    val distance by metronomeViewModel.distance.collectAsState()

    val isVisualEnabled by settingsViewModel.isVisualEnabled.collectAsState()
    val isAudioEnabled by settingsViewModel.isAudioEnabled.collectAsState()
    val isVibrationEnabled by settingsViewModel.isVibrationEnabled.collectAsState()

    val defaultBpm by settingsViewModel.defaultBpm.collectAsState()

    // --- Feedback Controllers ---
    val context = LocalContext.current
    val hapticsController = remember { HapticsController(context) }
    val audioMetronome = remember { AudioMetronome() }
    val lifecycleOwner = LocalLifecycleOwner.current

    // --- Lifecycle Awareness: Pause when app goes to background ---
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                Log.d("SessionScreen", "App paused, pausing metronome")
                metronomeViewModel.pause()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // --- Keep Screen On Logic ---
    DisposableEffect(Unit) {
        val window = (context as? Activity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    // --- Connect Settings BPM to Metronome BPM ---
    LaunchedEffect(defaultBpm) {
        metronomeViewModel.setBpm(defaultBpm)
    }

    // Only auto-start if this is a brand new session (stopwatch is 0)
    // and it's not already running.
    LaunchedEffect(Unit) {
        if (!metronomeViewModel.isRunning.value && metronomeViewModel.stopwatch.value == 0L) {
            metronomeViewModel.toggle()
        }
    }

    // --- Cleanup when leaving screen ---
    DisposableEffect(Unit) {
        onDispose {
            if (metronomeViewModel.isRunning.value) {
                metronomeViewModel.pause()
            }
            hapticsController.cancel()
            audioMetronome.release()
        }
    }

    // Ensure haptics stop immediately when paused
    LaunchedEffect(isRunning) {
        if (!isRunning) {
            hapticsController.cancel()
        }
    }

    // --- Local UI State ---
    var showControls by remember(isVisualEnabled) { mutableStateOf(!isVisualEnabled) }
    var poleStrikes by remember { mutableIntStateOf(beatCount) }

    LaunchedEffect(isRunning, isVisualEnabled) {
        if (isRunning && isVisualEnabled) {
            showControls = false
        }
    }

    // --- Feedback Logic (per beat) ---
    LaunchedEffect(beatCount) {
        if (!isRunning || beatCount <= 0) return@LaunchedEffect

        poleStrikes = beatCount

        if (isVibrationEnabled) {
            Log.d("SessionScreen", "Vibrating on beat $beatCount")
            hapticsController.vibrate(30)
        }
        if (isAudioEnabled) {
            audioMetronome.playBeep()
        }
    }

    // --- UI Layout ---
    val isTappable = isVisualEnabled

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(
                enabled = isTappable,
                onClick = { showControls = !showControls },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        if (isVisualEnabled && !showControls) {
            VisualModeView(
                time = stopwatch.toInt(),
                beatCount = beatCount
            )
        } else {
            DetailsView(
                time = stopwatch.toInt(),
                distance = distance,
                poleStrikes = poleStrikes,
                isPaused = !isRunning,
                onPauseToggle = { metronomeViewModel.toggle() },
                onEndSession = {
                    val finalTime = stopwatch.toInt()
                    val finalDistance = distance.roundToInt()
                    val finalStrikes = poleStrikes
                    
                    metronomeViewModel.saveSession(finalTime, finalDistance, finalStrikes)
                    metronomeViewModel.stop()
                    onEndSession(finalTime, finalDistance, finalStrikes)
                },
                onBack = {
                    onBack()
                }
            )
        }
    }
}

// --- VISUAL MODE VIEW ---
@Composable
private fun VisualModeView(time: Int, beatCount: Int) {
    val currentBeat = if (beatCount > 0) (beatCount - 1) % 4 else -1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = formatTime(time),
            fontSize = 28.sp,
            color = ColorWhite
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (index in 0..3) {
                MetronomeCircle(index = index, currentBeat = currentBeat)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Tap to show details",
            fontSize = 16.sp,
            color = ColorMutedGray
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun MetronomeCircle(index: Int, currentBeat: Int) {
    val isFilled = index <= currentBeat

    val targetColor = when (index) {
        0 -> ColorWhite
        1 -> ColorMint
        2 -> ColorWhite
        3 -> ColorTeal
        else -> ColorUnfilled
    }

    val backgroundColor by animateColorAsState(
        targetValue = if (isFilled) targetColor else ColorUnfilled,
        animationSpec = tween(200),
        label = "backgroundColor"
    )

    Box(
        modifier = Modifier
            .size(32.dp)
            .shadow(
                elevation = if (isFilled) 15.dp else 0.dp,
                shape = CircleShape,
                clip = false
            )
            .clip(CircleShape)
            .background(backgroundColor)
            .border(
                width = if (isFilled) 0.dp else 2.dp,
                color = if (isFilled) Color.Transparent else ColorUnfilledBorder,
                shape = CircleShape
            )
    )
}

// --- DETAILS VIEW ---
@Composable
private fun DetailsView(
    time: Int,
    distance: Double,
    poleStrikes: Int,
    isPaused: Boolean,
    onPauseToggle: () -> Unit,
    onEndSession: () -> Unit,
    onBack: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val backButtonIconSize = screenHeight * 0.12f
    val backButtonTouchTarget = screenHeight * 0.15f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = screenHeight * 0.02f)
    ) {

        val backInteractionSource = remember { MutableInteractionSource() }
        val isBackPressed by backInteractionSource.collectIsPressedAsState()
        val backButtonColor by animateColorAsState(
            targetValue = if (isBackPressed) EmeraldDark else ColorEmeraldGreen,
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

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Time",
                    color = ColorLightGray,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatTime(time),
                    color = ColorWhite,
                    fontSize = 44.sp
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Distance", color = ColorLightGray, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("${distance.roundToInt()}m", color = ColorWhite, fontSize = 26.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Strikes", color = ColorLightGray, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("$poleStrikes", color = ColorWhite, fontSize = 26.sp)
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onPauseToggle,
                    modifier = Modifier.size(54.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = ColorButtonDarkGray)
                ) {
                    Icon(
                        imageVector = if (isPaused) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                        contentDescription = if (isPaused) "Play" else "Pause",
                        tint = ColorWhite,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Button(
                    onClick = onEndSession,
                    modifier = Modifier.size(54.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = ColorButtonRed)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "End Session",
                        tint = ColorWhite,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}
