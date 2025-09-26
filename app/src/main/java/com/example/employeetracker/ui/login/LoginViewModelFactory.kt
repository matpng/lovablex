package com.example.employeetracker.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.employeetracker.data.database.AppDatabase
import com.example.employeetracker.data.repository.EmployeeRepository

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val database = AppDatabase.getDatabase(context)
            val repository = EmployeeRepository(
                database.employeeDao(),
                database.locationDao(),
                database.trackingSessionDao()
            )
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}