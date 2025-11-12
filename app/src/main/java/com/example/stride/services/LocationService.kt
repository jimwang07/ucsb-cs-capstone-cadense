package com.example.stride.services

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationService(private val context: Context) {
    private val fused = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getLocationUpdates(): Flow<Location> = callbackFlow {
        val req = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
            .setMinUpdateIntervalMillis(500L)
            .setWaitForAccurateLocation(true)
            .setMaxUpdateDelayMillis(0L)
            .build()

        val cb = object : LocationCallback() {
            override fun onLocationAvailability(av: LocationAvailability) {
                // This log is now our primary way to know if system settings are correct
                Log.d("LocationService", "onLocationAvailability: isLocationAvailable=${av.isLocationAvailable}")
            }
            override fun onLocationResult(res: LocationResult) {
                if (res.locations.isEmpty()) Log.d("LocationService", "empty location batch received")
                res.locations.forEach { trySend(it).isSuccess }
            }
        }

        // The checkLocationSettings API is not supported on Wear OS.
        // We proceed directly to requesting updates.
        Log.d("LocationService", "Requesting current location to prime the fix...")
        fused.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
            .addOnSuccessListener { if (it != null) trySend(it).isSuccess }
            .addOnFailureListener { Log.w("LocationService", "getCurrentLocation failed", it) }

        Log.d("LocationService", "Starting continuous location updates...")
        fused.requestLocationUpdates(req, cb, Looper.getMainLooper())

        awaitClose {
            fused.removeLocationUpdates(cb)
            Log.d("LocationService", "Location updates removed")
        }
    }
}
