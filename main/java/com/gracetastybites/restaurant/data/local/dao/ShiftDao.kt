package com.gracetastybites.restaurant.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gracetastybites.restaurant.data.local.entity.Shift
import java.util.Date

@Dao
interface ShiftDao {
    @Query("SELECT * FROM shifts ORDER BY date DESC")
    fun getAllShifts(): LiveData<List<Shift>>

    @Query("SELECT * FROM shifts WHERE staffId = :staffId ORDER BY date DESC")
    fun getShiftsByStaffId(staffId: Long): LiveData<List<Shift>>

    @Query("SELECT * FROM shifts WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getShiftsByDateRange(startDate: Date, endDate: Date): LiveData<List<Shift>>

    @Query("SELECT * FROM shifts WHERE staffId = :staffId AND date BETWEEN :startDate AND :endDate")
    fun getStaffShiftsByMonth(staffId: Long, startDate: Date, endDate: Date): LiveData<List<Shift>>

    @Query("SELECT COUNT(*) FROM shifts WHERE staffId = :staffId AND isCompleted = 1 AND date BETWEEN :startDate AND :endDate")
    suspend fun countCompletedShiftsInMonth(staffId: Long, startDate: Date, endDate: Date): Int

    @Insert
    suspend fun insert(shift: Shift): Long

    @Update
    suspend fun update(shift: Shift)

    @Delete
    suspend fun delete(shift: Shift)

    @Query("UPDATE shifts SET isAccepted = :accepted WHERE shiftId = :shiftId")
    suspend fun updateShiftAcceptance(shiftId: Long, accepted: Boolean)

    @Query("UPDATE shifts SET isCompleted = 1 WHERE shiftId = :shiftId")
    suspend fun markShiftCompleted(shiftId: Long)
}