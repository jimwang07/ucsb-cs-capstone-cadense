//package com.example.testlockscreen.presentation.ui
//
//import androidx.compose.animation.animateColorAsState
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.gestures.detectDragGestures
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.interaction.collectIsPressedAsState
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.ChevronLeft
//import androidx.compose.material.icons.filled.Remove
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.DisposableEffect
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableFloatStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.rotate
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.StrokeCap
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
//import androidx.compose.ui.graphics.nativeCanvas
//import androidx.compose.ui.graphics.toArgb
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.em
//import androidx.compose.ui.unit.sp
//import androidx.wear.compose.material.Icon
//import androidx.wear.compose.material.Text
//import com.example.testlockscreen.haptics.HapticsController
//import com.example.testlockscreen.presentation.theme.EmeraldDark
//import com.example.testlockscreen.presentation.theme.EmeraldGreen
//import kotlin.math.atan2
//import kotlin.math.cos
//import kotlin.math.sin
//
//// Color Palette
//private val EmeraldLight = Color(0xFF6EE7B7)
//private val EmeraldDim = Color(0xFF059669)
//private val GrayDark = Color(0xFF1F2937)
//private val GrayMid = Color(0xFF374151)
//private val GrayBorder = Color(0xFF4B5563)
//private val Black = Color(0xFF000000)
//private val White = Color(0xFFFFFFFF)
//private val Emerald100 = Color(0xFFD1FAE5)
//
//
//@Composable
//fun AdjustMetronomeScreen(
//    bpm: Int,
//    onBpmChange: (Int) -> Unit,
//    onBack: () -> Unit
//) {
//    val context = LocalContext.current
//    val hapticsController = remember { HapticsController(context) }
//
//    DisposableEffect(Unit) {
//        onDispose {
//            hapticsController.cancel()
//        }
//    }
//
//    val configuration = LocalConfiguration.current
//    val screenHeight = configuration.screenHeightDp.dp
//    val screenWidth = configuration.screenWidthDp.dp
//
//    val minValue = 60
//    val maxValue = 180
//    val divider = 300f / (maxValue - minValue)
//
//    var rotationAngle by remember { mutableFloatStateOf(0f) }
//    var isDragging by remember { mutableStateOf(false) }
//
//    LaunchedEffect(bpm, isDragging) {
//        if (!isDragging) {
//            rotationAngle = ((bpm - minValue) * divider) - 150f
//        }
//    }
//
//    val animatedRotation by animateFloatAsState(
//        targetValue = rotationAngle,
//        animationSpec = tween(durationMillis = if (isDragging) 0 else 300),
//        label = "RotationAnimation"
//    )
//
//    val backButtonIconSize = screenHeight * 0.12f
//    val backButtonTouchTarget = screenHeight * 0.15f
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Black)
//            .padding(all = screenHeight * 0.02f) // Consistent padding with SettingsScreen
//    ) {
//        // Back Button (Matching SettingsScreen exactly)
//        val backInteractionSource = remember { MutableInteractionSource() }
//        val isBackPressed by backInteractionSource.collectIsPressedAsState()
//        val backButtonColor by animateColorAsState(
//            targetValue = if (isBackPressed) EmeraldLight else EmeraldGreen,
//            animationSpec = tween(durationMillis = 200),
//            label = "BackButtonColor"
//        )
//
//        Box(
//            modifier = Modifier
//                .align(Alignment.CenterStart)
//                .offset(x = (-8).dp)
//                .size(backButtonTouchTarget)
//                .clip(CircleShape)
//                .clickable(interactionSource = backInteractionSource, indication = null, onClick = onBack),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = Icons.Default.ChevronLeft,
//                contentDescription = "Back",
//                modifier = Modifier.size(backButtonIconSize),
//                tint = backButtonColor
//            )
//        }
//
//        // Wheel Container
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier
//                .align(Alignment.Center) // Centered within the padded root Box
//                .size(screenWidth * 0.75f)
//                .pointerInput(Unit) {
//                    detectDragGestures(
//                        onDragStart = { isDragging = true },
//                        onDragEnd = { isDragging = false },
//                        onDrag = { change, _ ->
//                            val width = size.width.toFloat()
//                            val height = size.height.toFloat()
//                            val angle = calculateAngle(change.position.x, change.position.y, width, height)
//
//                            if (angle >= -150 && angle <= 150) {
//                                rotationAngle = angle
//                                val valueRangeDegrees = angle + 150
//                                val newBpm = ((valueRangeDegrees / divider) + minValue).toInt()
//                                if (newBpm != bpm) {
//                                    hapticsController.vibrate(15)
//                                    onBpmChange(newBpm.coerceIn(minValue, maxValue))
//                                }
//                            }
//                        }
//                    )
//                }
//        ) {
//            // Rotating Layer
//            RotatingWheel(
//                rotationDegrees = animatedRotation,
//                isDragging = isDragging
//            )
//
//            // Stationary Layer
//            CenterControls(
//                bpm = bpm,
//                onBpmChange = onBpmChange,
//                hapticsController = hapticsController
//            )
//        }
//    }
//}
//
//@Composable
//private fun RotatingWheel(rotationDegrees: Float, isDragging: Boolean) {
//    val outerRingWidth = 4.dp
//    val shadowElevation = if (isDragging) 30.dp else 20.dp
//    val shadowColor = if (isDragging) EmeraldGreen.copy(alpha = 0.5f) else EmeraldGreen.copy(alpha = 0.3f)
//
//    Canvas(
//        modifier = Modifier
//            .fillMaxSize()
//            .rotate(rotationDegrees)
//            .shadow(
//                elevation = shadowElevation,
//                shape = CircleShape,
//                clip = false,
//                ambientColor = shadowColor,
//                spotColor = shadowColor
//            )
//            .background(EmeraldGreen.copy(alpha = 0.2f), CircleShape)
//    ) {
//        val radius = size.minDimension / 2
//        // Outer Ring
//        drawCircle(
//            color = EmeraldGreen,
//            radius = radius,
//            style = Stroke(width = outerRingWidth.toPx())
//        )
//
//        // Inner Decorative Ring
//        drawCircle(
//            color = EmeraldGreen.copy(alpha = 0.3f),
//            radius = radius - 19.5.dp.toPx(),
//            style = Stroke(width = 1.dp.toPx())
//        )
//        // Tick Marks
//        val tickPlacementRadius = radius - 5.dp.toPx() // Position ticks between outer and inner ring
//        for (i in 0 until 24) {
//            val angleRad = Math.toRadians(i * 15.0)
//            val isLargeTick = i % 6 == 0
//
//            val tickWidth = if (isLargeTick) 2.dp.toPx() else 1.5.dp.toPx()
//            val tickLength = if (isLargeTick) 8.dp.toPx() else 5.dp.toPx()
//            val tickColor = if (isLargeTick) EmeraldGreen else EmeraldGreen.copy(alpha = 0.7f)
//            val tickGlow = if (isLargeTick) EmeraldGreen.copy(alpha = 0.5f) else Color.Transparent
//
//            val cosAngle = cos(angleRad).toFloat()
//            val sinAngle = sin(angleRad).toFloat()
//
//            // Center of the tangential tick
//            val tickCenterX = center.x + tickPlacementRadius * cosAngle
//            val tickCenterY = center.y + tickPlacementRadius * sinAngle
//
//            // Start and end points of the tick, tangential to the circle
//            val start = Offset(
//                x = tickCenterX - (tickLength / 2) * sinAngle,
//                y = tickCenterY + (tickLength / 2) * cosAngle
//            )
//            val end = Offset(
//                x = tickCenterX + (tickLength / 2) * sinAngle,
//                y = tickCenterY - (tickLength / 2) * cosAngle
//            )
//
//            if (isLargeTick) {
//                drawIntoCanvas { canvas ->
//                    val paint = android.graphics.Paint().apply {
//                        this.color = tickGlow.toArgb()
//                        this.strokeWidth = tickWidth
//                        this.style = android.graphics.Paint.Style.STROKE
//                        this.strokeCap = android.graphics.Paint.Cap.ROUND
//                        setShadowLayer(4.dp.toPx(), 0f, 0f, tickGlow.toArgb())
//                    }
//                    canvas.nativeCanvas.drawLine(start.x, start.y, end.x, end.y, paint)
//                }
//            }
//
//            drawLine(
//                color = tickColor,
//                start = start,
//                end = end,
//                strokeWidth = tickWidth,
//                cap = StrokeCap.Round
//            )
//        }
//
//        // Top Indicator
//        val indicatorWidth = 3.dp.toPx()
//        val indicatorColor = White
//        val indicatorGlow = White.copy(alpha = 0.6f)
//
//        // Stationary indicator pointing outwards from the top
//        val angleRad = Math.toRadians(-90.0)
//        val cosAngle = cos(angleRad).toFloat()
//        val sinAngle = sin(angleRad).toFloat()
//
//        val startRadius = radius - 10.5.dp.toPx() // Start inside inner ring
//        val endRadius = radius + 4.dp.toPx()   // End outside outer ring
//
//        val start = Offset(
//            x = center.x + startRadius * cosAngle,
//            y = center.y + startRadius * sinAngle
//        )
//        val end = Offset(
//            x = center.x + endRadius * cosAngle,
//            y = center.y + endRadius * sinAngle
//        )
//
//        // Draw glow
//        drawIntoCanvas { canvas ->
//            val paint = android.graphics.Paint().apply {
//                color = indicatorGlow.toArgb()
//                strokeWidth = indicatorWidth * 1.5f
//                strokeCap = android.graphics.Paint.Cap.ROUND
//                style = android.graphics.Paint.Style.STROKE
//                setShadowLayer(6.dp.toPx(), 0f, 0f, indicatorGlow.toArgb())
//            }
//            canvas.nativeCanvas.drawLine(start.x, start.y, end.x, end.y, paint)
//        }
//
//        // Draw main indicator line
//        drawLine(
//            color = indicatorColor,
//            start = start,
//            end = end,
//            strokeWidth = indicatorWidth,
//            cap = StrokeCap.Round
//        )
//    }
//}
//
//@Composable
//private fun CenterControls(
//    bpm: Int,
//    onBpmChange: (Int) -> Unit,
//    hapticsController: HapticsController
//) {
//    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
//    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
//
//    val buttonSize = screenWidth * 0.144f
//    val iconSize = buttonSize * 0.5f
//
//    // This is the size of the parent Box that holds the wheel and these controls
//    val wheelContainerWidth = screenWidth * 0.75f
//    // This is the radius where the center of the buttons should be.
//    // It's the radius of the inner ring.
//    val buttonPlacementRadius = (wheelContainerWidth / 2) - 19.5.dp
//
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        // BPM Display is the first child, so it's in the center and at the bottom of the z-stack.
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "BPM",
//                color = White.copy(alpha = 0.6f),
//                fontSize = (screenHeight * 0.035f).value.sp,
//                letterSpacing = 0.1.em,
//                textAlign = TextAlign.Center
//            )
//            Text(
//                text = bpm.toString(),
//                style = TextStyle(
//                    brush = Brush.verticalGradient(
//                        colors = listOf(White, Emerald100)
//                    ),
//                    fontWeight = FontWeight.Bold,
//                    fontSize = (screenHeight * 0.15f).value.sp,
//                    textAlign = TextAlign.Center
//                )
//            )
//        }
//
//        // Minus Button
//        val minusInteractionSource = remember { MutableInteractionSource() }
//        val isMinusPressed by minusInteractionSource.collectIsPressedAsState()
//        val minusBgBrush = if (isMinusPressed) {
//            Brush.verticalGradient(listOf(GrayMid.copy(alpha = 0.8f), GrayDark.copy(alpha = 0.8f)))
//        } else {
//            Brush.verticalGradient(listOf(GrayMid, GrayDark))
//        }
//
//        Box(
//            modifier = Modifier
//                .offset(x = -buttonPlacementRadius)
//                .size(buttonSize)
//                .clickable(
//                    enabled = bpm > 60,
//                    onClick = {
//                        hapticsController.vibrate(15)
//                        onBpmChange(bpm - 1)
//                    },
//                    interactionSource = minusInteractionSource,
//                    indication = null
//                )
//                .shadow(elevation = 4.dp, shape = CircleShape, spotColor = Black.copy(alpha = 0.3f))
//                .background(minusBgBrush, CircleShape)
//                .border(1.dp, GrayBorder, CircleShape),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = Icons.Default.Remove,
//                contentDescription = "Decrease BPM",
//                modifier = Modifier.size(iconSize),
//                tint = if (isMinusPressed) EmeraldLight else EmeraldGreen
//            )
//        }
//
//        // Plus Button
//        val plusInteractionSource = remember { MutableInteractionSource() }
//        val isPlusPressed by plusInteractionSource.collectIsPressedAsState()
//        val plusBgBrush = if (isPlusPressed) {
//            Brush.verticalGradient(listOf(EmeraldLight, EmeraldDim))
//        } else {
//            Brush.verticalGradient(listOf(EmeraldDark, EmeraldDim))
//        }
//
//        Box(
//            modifier = Modifier
//                .offset(x = buttonPlacementRadius)
//                .size(buttonSize)
//                .clickable(
//                    enabled = bpm < 180,
//                    onClick = {
//                        hapticsController.vibrate(15)
//                        onBpmChange(bpm + 1)
//                    },
//                    interactionSource = plusInteractionSource,
//                    indication = null
//                )
//                .shadow(elevation = 4.dp, shape = CircleShape, spotColor = EmeraldDark.copy(alpha = 0.4f))
//                .background(plusBgBrush, CircleShape)
//                .border(1.dp, EmeraldGreen, CircleShape),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = Icons.Default.Add,
//                contentDescription = "Increase BPM",
//                modifier = Modifier.size(iconSize),
//                tint = White
//            )
//        }
//    }
//}
//
//private fun calculateAngle(x: Float, y: Float, width: Float, height: Float): Float {
//    val px = (x / width) - 0.5f
//    val py = (1 - y / height) - 0.5f
//    var angle = -(Math.toDegrees(atan2(py.toDouble(), px.toDouble()))).toFloat() + 90
//    if (angle > 180) angle -= 360
//    return angle
//}
