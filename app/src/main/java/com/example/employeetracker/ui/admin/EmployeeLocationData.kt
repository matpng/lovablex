package com.example.employeetracker.ui.admin

import com.example.employeetracker.data.database.Employee
import com.example.employeetracker.data.database.LocationRecord
import com.example.employeetracker.data.database.TrackingSession

data class EmployeeLocationData(
    val employee: Employee,
    val lastLocation: LocationRecord?,
    val activeSession: TrackingSession?
)