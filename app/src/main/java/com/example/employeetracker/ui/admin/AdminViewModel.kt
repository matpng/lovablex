package com.example.employeetracker.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employeetracker.data.repository.EmployeeRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AdminViewModel(private val repository: EmployeeRepository) : ViewModel() {
    
    private val _employeeLocations = MutableLiveData<List<EmployeeLocationData>>()
    val employeeLocations: LiveData<List<EmployeeLocationData>> = _employeeLocations
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    fun loadEmployeeLocations() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val employees = repository.getAllEmployees().first()
                val employeeLocationData = mutableListOf<EmployeeLocationData>()
                
                employees.forEach { employee ->
                    // Skip admin users
                    if (!employee.isAdmin) {
                        val lastLocation = repository.getLastLocationByEmployee(employee.id)
                        val activeSession = repository.getActiveSession(employee.id)
                        
                        employeeLocationData.add(
                            EmployeeLocationData(
                                employee = employee,
                                lastLocation = lastLocation,
                                activeSession = activeSession
                            )
                        )
                    }
                }
                
                _employeeLocations.value = employeeLocationData
            } catch (e: Exception) {
                _employeeLocations.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}