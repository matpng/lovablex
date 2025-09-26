package com.example.employeetracker.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey
    val id: String,
    val email: String,
    val password: String, // In a real app, this would be hashed
    val name: String,
    val isAdmin: Boolean = false,
    val createdAt: Date = Date()
)