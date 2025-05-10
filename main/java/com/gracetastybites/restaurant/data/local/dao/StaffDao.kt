package com.gracetastybites.restaurant.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gracetastybites.restaurant.data.local.entity.Staff
import com.gracetastybites.restaurant.data.local.entity.StaffRole

@Dao
interface StaffDao {
    @Query("SELECT * FROM staff WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActiveStaff(): LiveData<List<Staff>>

    @Query("SELECT * FROM staff WHERE staffId = :staffId")
    fun getStaffById(staffId: Long): LiveData<Staff>

    @Query("SELECT * FROM staff WHERE role = :role AND isActive = 1")
    fun getStaffByRole(role: StaffRole): LiveData<List<Staff>>

    @Query("SELECT * FROM staff WHERE username = :username LIMIT 1")
    suspend fun getStaffByUsername(username: String): Staff?

    @Query("SELECT * FROM staff WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): Staff?

    @Insert
    suspend fun insert(staff: Staff): Long

    @Update
    suspend fun update(staff: Staff)

    @Query("UPDATE staff SET isActive = 0 WHERE staffId = :staffId")
    suspend fun deactivateStaff(staffId: Long)
}