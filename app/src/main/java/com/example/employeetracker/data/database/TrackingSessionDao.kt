package com.example.employeetracker.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackingSessionDao {
    @Query("SELECT * FROM tracking_sessions WHERE employeeId = :employeeId AND isActive = 1 LIMIT 1")
    suspend fun getActiveSession(employeeId: String): TrackingSession?
    
    @Query("SELECT * FROM tracking_sessions WHERE employeeId = :employeeId ORDER BY checkInTime DESC")
    fun getSessionsByEmployee(employeeId: String): Flow<List<TrackingSession>>
    
    @Query("SELECT * FROM tracking_sessions ORDER BY checkInTime DESC")
    fun getAllSessions(): Flow<List<TrackingSession>>
    
    @Insert
    suspend fun insertSession(session: TrackingSession): Long
    
    @Update
    suspend fun updateSession(session: TrackingSession)
    
    @Query("UPDATE tracking_sessions SET checkOutTime = :checkOutTime, isActive = 0 WHERE id = :sessionId")
    suspend fun endSession(sessionId: Long, checkOutTime: java.util.Date)
}