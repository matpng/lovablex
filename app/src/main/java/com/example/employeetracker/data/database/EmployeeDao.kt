package com.example.employeetracker.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM employees WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): Employee?
    
    @Query("SELECT * FROM employees WHERE id = :id LIMIT 1")
    suspend fun getEmployeeById(id: String): Employee?
    
    @Query("SELECT * FROM employees")
    fun getAllEmployees(): Flow<List<Employee>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: Employee)
    
    @Update
    suspend fun updateEmployee(employee: Employee)
    
    @Delete
    suspend fun deleteEmployee(employee: Employee)
}