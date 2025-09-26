package com.example.employeetracker.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM location_records WHERE employeeId = :employeeId ORDER BY timestamp DESC")
    fun getLocationsByEmployee(employeeId: String): Flow<List<LocationRecord>>
    
    @Query("SELECT * FROM location_records ORDER BY timestamp DESC")
    fun getAllLocations(): Flow<List<LocationRecord>>
    
    @Query("SELECT * FROM location_records WHERE employeeId = :employeeId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastLocationByEmployee(employeeId: String): LocationRecord?
    
    @Insert
    suspend fun insertLocation(location: LocationRecord)
    
    @Insert
    suspend fun insertLocations(locations: List<LocationRecord>)
    
    @Query("DELETE FROM location_records WHERE employeeId = :employeeId")
    suspend fun deleteLocationsByEmployee(employeeId: String)
    
    @Query("DELETE FROM location_records WHERE timestamp < :cutoffDate")
    suspend fun deleteOldLocations(cutoffDate: java.util.Date)
}