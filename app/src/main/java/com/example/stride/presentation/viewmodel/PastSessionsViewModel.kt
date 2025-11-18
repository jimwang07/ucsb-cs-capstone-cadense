package com.example.stride.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.stride.data.dao.SessionDao
import com.example.stride.presentation.ui.SessionData
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PastSessionsViewModel(
    private val sessionDao: SessionDao
) : ViewModel() {

    // Expose sessions as StateFlow<List<SessionData>> for the UI
    val sessions: StateFlow<List<SessionData>> =
        sessionDao
            .getAllSessions()              // Flow<List<Session>>
            .map { list ->
                list.map { s ->
                    SessionData(
                        time = s.duration,          // map duration -> time (seconds)
                        distance = s.distance,
                        poleStrikes = s.poleStrikes
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}

/**
 * Factory so we can create PastSessionsViewModel with a SessionDao.
 * We'll use this in the NavHost when calling viewModel(...).
 */
class PastSessionsViewModelFactory(
    private val sessionDao: SessionDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PastSessionsViewModel::class.java)) {
            return PastSessionsViewModel(sessionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
