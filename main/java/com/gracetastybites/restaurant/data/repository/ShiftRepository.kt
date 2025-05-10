package com.gracetastybites.restaurant.data.repository

import androidx.lifecycle.LiveData
import com.gracetastybites.restaurant.data.local.dao.ShiftDao
import com.gracetastybites.restaurant.data.local.entity.Shift
import java.util.Calendar
import java.util.Date

class ShiftRepository(private val shiftDao: ShiftDao) {

    val allShifts: LiveData<List<Shift>> = shiftDao.getAllShifts()

    fun getShiftsByStaffId(staffId: Long): LiveData<List<Shift>> {
        return shiftDao.getShiftsByStaffId(staffId)
    }

    fun getShiftsByDateRange(startDate: Date, endDate: Date): LiveData<List<Shift>> {
        return shiftDao.getShiftsByDateRange(startDate, endDate)
    }

    fun getStaffShiftsByMonth(staffId: Long, month: Int, year: Int): LiveData<List<Shift>> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1, 0, 0, 0) // Month is 0-indexed in Calendar
        val startDate = calendar.time

        calendar.set(year, month - 1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59)
        val endDate = calendar.time

        return shiftDao.getStaffShiftsByMonth(staffId, startDate, endDate)
    }

    suspend fun countCompletedShiftsInMonth(staffId: Long, month: Int, year: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1, 0, 0, 0)
        val startDate = calendar.time

        calendar.set(year, month - 1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59)
        val endDate = calendar.time

        return shiftDao.countCompletedShiftsInMonth(staffId, startDate, endDate)
    }

    suspend fun assignShift(shift: Shift): Long {
        return shiftDao.insert(shift)
    }

    suspend fun updateShift(shift: Shift) {
        shiftDao.update(shift)
    }

    suspend fun deleteShift(shift: Shift) {
        shiftDao.delete(shift)
    }

    suspend fun acceptShift(shiftId: Long, accepted: Boolean) {
        shiftDao.updateShiftAcceptance(shiftId, accepted)
    }

    suspend fun markShiftCompleted(shiftId: Long) {
        shiftDao.markShiftCompleted(shiftId)
    }
}