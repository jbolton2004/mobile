package com.gracetastybites.restaurant.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gracetastybites.restaurant.data.local.entity.Shift
import com.gracetastybites.restaurant.data.repository.ShiftRepository
import kotlinx.coroutines.launch
import java.util.*

class ShiftViewModel(private val repository: ShiftRepository) : ViewModel() {

    val allShifts: LiveData<List<Shift>> = repository.allShifts

    private val _selectedShifts = MutableLiveData<List<Shift>>()
    val selectedShifts: LiveData<List<Shift>> = _selectedShifts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _operationSuccessful = MutableLiveData<Boolean>()
    val operationSuccessful: LiveData<Boolean> = _operationSuccessful

    fun getShiftsByStaffId(staffId: Long) {
        repository.getShiftsByStaffId(staffId).observeForever { shifts ->
            _selectedShifts.value = shifts
        }
    }

    fun getShiftsByDateRange(startDate: Date, endDate: Date) {
        repository.getShiftsByDateRange(startDate, endDate).observeForever { shifts ->
            _selectedShifts.value = shifts
        }
    }

    fun getStaffShiftsByMonth(staffId: Long, month: Int, year: Int) {
        repository.getStaffShiftsByMonth(staffId, month, year).observeForever { shifts ->
            _selectedShifts.value = shifts
        }
    }

    fun assignShift(shift: Shift) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                repository.assignShift(shift)
                _operationSuccessful.value = true
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Failed to assign shift: ${e.message}"
                _operationSuccessful.value = false
                _isLoading.value = false
            }
        }
    }

    fun updateShift(shift: Shift) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                repository.updateShift(shift)
                _operationSuccessful.value = true
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update shift: ${e.message}"
                _operationSuccessful.value = false
                _isLoading.value = false
            }
        }
    }

    fun deleteShift(shift: Shift) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                repository.deleteShift(shift)
                _operationSuccessful.value = true
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete shift: ${e.message}"
                _operationSuccessful.value = false
                _isLoading.value = false
            }
        }
    }

    fun acceptShift(shiftId: Long, accepted: Boolean) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                repository.acceptShift(shiftId, accepted)
                _operationSuccessful.value = true
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update shift acceptance: ${e.message}"
                _operationSuccessful.value = false
                _isLoading.value = false
            }
        }
    }

    fun markShiftCompleted(shiftId: Long) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                repository.markShiftCompleted(shiftId)
                _operationSuccessful.value = true
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Failed to mark shift as completed: ${e.message}"
                _operationSuccessful.value = false
                _isLoading.value = false
            }
        }
    }

    suspend fun countCompletedShiftsInMonth(staffId: Long, month: Int, year: Int): Int {
        return repository.countCompletedShiftsInMonth(staffId, month, year)
    }
}