package com.example.stride.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.stride.data.dao.SessionDao
import com.example.stride.data.entities.Session
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SessionDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var sessionDao: SessionDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        sessionDao = db.sessionDao()
    }



    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetSession() = runBlocking {
        // Arrange
        val originalSession = Session(timestamp = 1L, duration = 100, poleStrikes = 500)
        sessionDao.insert(originalSession)

        // Act
        val retrievedSession = sessionDao.getAllSessions().first()[0]

        // Assert
        // We compare the original session (with the ID copied from the retrieved one) to the retrieved one.
        assertEquals(originalSession.copy(id = retrievedSession.id), retrievedSession)
    }
}
