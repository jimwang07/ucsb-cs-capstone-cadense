package com.example.testlockscreen.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.example.testlockscreen.data.entities.SensorSample
import com.example.testlockscreen.presentation.viewmodel.ImuGraphViewModel
import kotlin.math.abs

@Composable
fun ImuGraphScreen(
    viewModel: ImuGraphViewModel,
    onBack: () -> Unit
) {
    val samples by viewModel.samples.collectAsState()
    val latestSample = samples.lastOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "IMU Live Graph", modifier = Modifier.padding(bottom = 4.dp))
            if (latestSample != null) {
                Text(
                    text = String.format(
                        "x: %.2f  y: %.2f  z: %.2f",
                        latestSample.value1 ?: 0f,
                        latestSample.value2 ?: 0f,
                        latestSample.value3 ?: 0f
                    )
                )
            } else {
                Text(text = "Move the watch to start streaming data")
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(Color(0xFF1B1B1B), RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            val centerY = size.height / 2f
            drawLine(
                color = Color.DarkGray,
                start = Offset(0f, centerY),
                end = Offset(size.width, centerY),
                strokeWidth = 1.dp.toPx()
            )

            drawAxis(samples, AxisDescriptor(Color.Red) { it.value1 })
            drawAxis(samples, AxisDescriptor(Color.Green) { it.value2 })
            drawAxis(samples, AxisDescriptor(Color.Blue) { it.value3 })
        }

        Legend()

        Button(
            onClick = onBack,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF29EAB6))
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }
    }
}

private data class AxisDescriptor(
    val color: Color,
    val extractor: (SensorSample) -> Float?
)

private fun DrawScope.drawAxis(samples: List<SensorSample>, axisDescriptor: AxisDescriptor) {
    if (samples.size < 2) return

    val values = samples.mapNotNull(axisDescriptor.extractor)
    if (values.isEmpty()) return

    val maxAbs = values.maxOf { abs(it) }.coerceAtLeast(1f)
    val centerY = size.height / 2f
    val verticalRange = centerY * 0.8f
    val stepX = size.width / (samples.size - 1)

    var previousPoint: Offset? = null
    samples.forEachIndexed { index, sample ->
        val value = axisDescriptor.extractor(sample) ?: return@forEachIndexed
        val normalizedY = (value / maxAbs).coerceIn(-1f, 1f)
        val currentPoint = Offset(
            x = stepX * index,
            y = centerY - normalizedY * verticalRange
        )
        if (previousPoint != null) {
            drawLine(
                color = axisDescriptor.color,
                start = previousPoint!!,
                end = currentPoint,
                strokeWidth = 2.dp.toPx()
            )
        }
        previousPoint = currentPoint
    }
}

@Composable
private fun Legend() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendItem(color = Color.Red, label = "X")
        LegendItem(color = Color.Green, label = "Y")
        LegendItem(color = Color.Blue, label = "Z")
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Text(text = label)
    }
}
