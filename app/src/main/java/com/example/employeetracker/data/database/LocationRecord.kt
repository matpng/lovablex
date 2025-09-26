package com.example.employeetracker.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "location_records",
    foreignKeys = [ForeignKey(
        entity = Employee::class,
        parentColumns = ["id"],
        childColumns = ["employeeId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class LocationRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val employeeId: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Date = Date(),
    val accuracy: Float? = null,
    val address: String? = null
)