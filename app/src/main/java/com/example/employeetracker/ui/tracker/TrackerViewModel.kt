package com.example.employeetracker.ui.tracker

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employeetracker.data.repository.EmployeeRepository
import kotlinx.coroutines.launch

class TrackerViewModel(private val repository: EmployeeRepository) : ViewModel() {
    
    private val _trackingStatus = MutableLiveData<Boolean>()
    val trackingStatus: LiveData<Boolean> = _trackingStatus
    
    private val _currentLocation = MutableLiveData<String?>()
    val currentLocation: LiveData<String?> = _currentLocation
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private var currentEmployeeId: String? = null
    
    fun setEmployeeId(employeeId: String) {
        currentEmployeeId = employeeId
    }
    
    fun checkTrackingStatus(employeeId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val activeSession = repository.getActiveSession(employeeId)
                _trackingStatus.value = activeSession != null
                
                // Get last known location
                val lastLocation = repository.getLastLocationByEmployee(employeeId)
                if (lastLocation != null) {
                    val locationText = "Lat: ${String.format("%.6f", lastLocation.latitude)}, " +
                                    "Lng: ${String.format("%.6f", lastLocation.longitude)}"
                    _currentLocation.value = locationText
                } else {
                    _currentLocation.value = "No location available"
                }
            } catch (e: Exception) {
                _trackingStatus.value = false
                _currentLocation.value = "Error loading location"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun startTracking(employeeId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // End any existing sessions first
                repository.endTrackingSession(employeeId)
                
                // Start new session
                repository.startTrackingSession(employeeId)
                _trackingStatus.value = true
            } catch (e: Exception) {
                // Handle error
                _trackingStatus.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun stopTracking(employeeId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.endTrackingSession(employeeId)
                _trackingStatus.value = false
            } catch (e: Exception) {
                // Handle error silently for stop operation
            } finally {
                _isLoading.value = false
            }
        }
    }
}