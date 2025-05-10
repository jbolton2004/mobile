package com.gracetastybites.restaurant.data.repository

import androidx.lifecycle.LiveData
import com.gracetastybites.restaurant.data.local.dao.StaffDao
import com.gracetastybites.restaurant.data.local.entity.Staff
import com.gracetastybites.restaurant.data.local.entity.StaffRole

class StaffRepository(private val staffDao: StaffDao) {

    val allActiveStaff: LiveData<List<Staff>> = staffDao.getAllActiveStaff()

    fun getStaffById(staffId: Long): LiveData<Staff> {
        return staffDao.getStaffById(staffId)
    }

    fun getStaffByRole(role: StaffRole): LiveData<List<Staff>> {
        return staffDao.getStaffByRole(role)
    }

    suspend fun login(username: String, password: String): Staff? {
        return staffDao.login(username, password)
    }

    suspend fun addStaff(staff: Staff): Long {
        return staffDao.insert(staff)
    }

    suspend fun updateStaff(staff: Staff) {
        staffDao.update(staff)
    }

    suspend fun deactivateStaff(staffId: Long) {
        staffDao.deactivateStaff(staffId)
    }

    suspend fun getStaffByUsername(username: String): Staff? {
        return staffDao.getStaffByUsername(username)
    }
}