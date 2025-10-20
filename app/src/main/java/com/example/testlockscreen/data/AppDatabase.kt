package com.example.testlockscreen.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testlockscreen.data.dao.SessionDao
import com.example.testlockscreen.data.dao.SensorSampleDao
import com.example.testlockscreen.data.entities.Session
import com.example.testlockscreen.data.entities.SensorSample

@Database(entities = [Session::class, SensorSample::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun sensorSampleDao(): SensorSampleDao
}
