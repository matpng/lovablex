package com.example.employeetracker.data.repository

import com.example.employeetracker.data.database.*
import kotlinx.coroutines.flow.Flow
import java.util.Date
import java.util.UUID

class EmployeeRepository(
    private val employeeDao: EmployeeDao,
    private val locationDao: LocationDao,
    private val trackingSessionDao: TrackingSessionDao
) {
    
    // Employee Authentication
    suspend fun login(email: String, password: String): Employee? {
        return employeeDao.login(email, password)
    }
    
    suspend fun getEmployeeById(id: String): Employee? {
        return employeeDao.getEmployeeById(id)
    }
    
    fun getAllEmployees(): Flow<List<Employee>> {
        return employeeDao.getAllEmployees()
    }
    
    // Location Management
    suspend fun insertLocation(employeeId: String, latitude: Double, longitude: Double, accuracy: Float? = null, address: String? = null) {
        val location = LocationRecord(
            employeeId = employeeId,
            latitude = latitude,
            longitude = longitude,
            accuracy = accuracy,
            address = address,
            timestamp = Date()
        )
        locationDao.insertLocation(location)
    }
    
    fun getLocationsByEmployee(employeeId: String): Flow<List<LocationRecord>> {
        return locationDao.getLocationsByEmployee(employeeId)
    }
    
    fun getAllLocations(): Flow<List<LocationRecord>> {
        return locationDao.getAllLocations()
    }
    
    suspend fun getLastLocationByEmployee(employeeId: String): LocationRecord? {
        return locationDao.getLastLocationByEmployee(employeeId)
    }
    
    // Tracking Session Management
    suspend fun startTrackingSession(employeeId: String): Long {
        val session = TrackingSession(employeeId = employeeId)
        return trackingSessionDao.insertSession(session)
    }
    
    suspend fun endTrackingSession(employeeId: String) {
        val activeSession = trackingSessionDao.getActiveSession(employeeId)
        activeSession?.let { session ->
            trackingSessionDao.endSession(session.id, Date())
        }
    }
    
    suspend fun getActiveSession(employeeId: String): TrackingSession? {
        return trackingSessionDao.getActiveSession(employeeId)
    }
    
    fun getSessionsByEmployee(employeeId: String): Flow<List<TrackingSession>> {
        return trackingSessionDao.getSessionsByEmployee(employeeId)
    }
    
    fun getAllSessions(): Flow<List<TrackingSession>> {
        return trackingSessionDao.getAllSessions()
    }
    
    // Demo data initialization
    suspend fun initializeWithDemoData() {
        val demoEmployees = listOf(
            Employee(
                id = UUID.randomUUID().toString(),
                email = "john@company.com",
                password = "password123",
                name = "John Doe",
                isAdmin = false
            ),
            Employee(
                id = UUID.randomUUID().toString(),
                email = "jane@company.com",
                password = "password123",
                name = "Jane Smith",
                isAdmin = false
            ),
            Employee(
                id = UUID.randomUUID().toString(),
                email = "admin@company.com",
                password = "admin123",
                name = "Admin User",
                isAdmin = true
            )
        )
        
        demoEmployees.forEach { employee ->
            employeeDao.insertEmployee(employee)
        }
    }
}