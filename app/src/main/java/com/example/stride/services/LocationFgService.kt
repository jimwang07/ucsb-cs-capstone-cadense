package com.example.stride.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.stride.R

class LocationFgService : Service() {
    override fun onCreate() {
        super.onCreate()
        val channelId = "loc_channel"
        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(
            NotificationChannel(channelId, "Location tracking", NotificationManager.IMPORTANCE_LOW)
        )
        val notif = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // Using existing launcher icon
            .setContentTitle("Tracking workout")
            .setContentText("Getting location")
            .setOngoing(true)
            .build()
        startForeground(42, notif)
    }
    override fun onStartCommand(i: Intent?, f: Int, id: Int) = START_STICKY
    override fun onBind(i: Intent?) = null
}
