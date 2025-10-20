package com.example.testlockscreen.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.testlockscreen.data.entities.SensorSample
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SensorManagerWrapper(private val context: Context) {

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    fun getAccelerometerSensorFlow(): Flow<SensorSample> = callbackFlow {
        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometerSensor == null) {
            Log.e("SensorManagerWrapper", "Accelerometer not available")
            close() // Close the flow if sensor is not available
            return@callbackFlow
        }

        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                    val timestamp = System.currentTimeMillis()
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]
                    val magnitude = Math.sqrt((x*x + y*y + z*z).toDouble()).toFloat()
                    trySend(SensorSample(timestamp = timestamp, type = "ACCELEROMETER", value1 = magnitude, value2 = x, value3 = y))
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Not used
            }
        }

        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME)

        awaitClose { sensorManager.unregisterListener(sensorEventListener) }
    }

    fun getHeartRateSensorFlow(): Flow<SensorSample> = callbackFlow {
        val heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        if (heartRateSensor == null) {
            Log.e("SensorManagerWrapper", "Heart Rate sensor not available")
            close()
            return@callbackFlow
        }

        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_HEART_RATE) {
                    val timestamp = System.currentTimeMillis()
                    val heartRate = event.values[0]
                    trySend(SensorSample(timestamp = timestamp, type = "HEART_RATE", value1 = heartRate, value2 = null, value3 = null))
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Not used
            }
        }

        sensorManager.registerListener(sensorEventListener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL)

        awaitClose { sensorManager.unregisterListener(sensorEventListener) }
    }
}
