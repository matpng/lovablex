package com.example.employeetracker.ui.tracker

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.employeetracker.data.database.AppDatabase
import com.example.employeetracker.data.repository.EmployeeRepository

class TrackerViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrackerViewModel::class.java)) {
            val database = AppDatabase.getDatabase(context)
            val repository = EmployeeRepository(
                database.employeeDao(),
                database.locationDao(),
                database.trackingSessionDao()
            )
            return TrackerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}