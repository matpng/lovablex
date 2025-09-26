package com.example.employeetracker.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employeetracker.data.database.AppDatabase
import com.example.employeetracker.data.database.Employee
import com.example.employeetracker.data.repository.EmployeeRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: EmployeeRepository) : ViewModel() {
    
    private val _loginResult = MutableLiveData<Result<Employee>>()
    val loginResult: LiveData<Result<Employee>> = _loginResult
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val employee = repository.login(email, password)
                if (employee != null) {
                    _loginResult.value = Result.success(employee)
                } else {
                    _loginResult.value = Result.failure(Exception("Invalid email or password"))
                }
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    suspend fun initializeDemoData() {
        try {
            repository.initializeWithDemoData()
        } catch (e: Exception) {
            // Demo data might already exist, ignore error
        }
    }
}