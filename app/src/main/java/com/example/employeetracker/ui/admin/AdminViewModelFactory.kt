package com.example.employeetracker.ui.admin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.employeetracker.data.database.AppDatabase
import com.example.employeetracker.data.repository.EmployeeRepository

class AdminViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
            val database = AppDatabase.getDatabase(context)
            val repository = EmployeeRepository(
                database.employeeDao(),
                database.locationDao(),
                database.trackingSessionDao()
            )
            return AdminViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}