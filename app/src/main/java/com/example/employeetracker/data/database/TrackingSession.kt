package com.example.employeetracker.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "tracking_sessions",
    foreignKeys = [ForeignKey(
        entity = Employee::class,
        parentColumns = ["id"],
        childColumns = ["employeeId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TrackingSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val employeeId: String,
    val checkInTime: Date = Date(),
    val checkOutTime: Date? = null,
    val isActive: Boolean = true
)