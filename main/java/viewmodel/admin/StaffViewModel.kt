package com.gracetastybites.restaurant.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gracetastybites.restaurant.data.local.entity.Staff
import com.gracetastybites.restaurant.data.local.entity.StaffRole
import com.gracetastybites.restaurant.data.repository.StaffRepository
import kotlinx.coroutines.launch

class StaffViewModel(private val repository: StaffRepository) : ViewModel() {

    val allActiveStaff: LiveData<List<Staff>> = repository.allActiveStaff

    private val _selectedStaff = MutableLiveData<Staff>()
    val selectedStaff: LiveData<Staff> = _selectedStaff

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _operationSuccessful = MutableLiveData<Boolean>()
    val operationSuccessful: LiveData<Boolean> = _operationSuccessful

    fun getStaffById(staffId: Long) {
        _isLoading.value = true
        repository.getStaffById(staffId).observeForever { staff ->
            _selectedStaff.value = staff
            _isLoading.value = false
        }
    }

    fun getStaffByRole(role: StaffRole): LiveData<List<Staff>> {
        return repository.getStaffByRole(role)
    }

    fun addStaff(staff: Staff) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Validate username uniqueness
                val existingStaff = repository.getStaffByUsername(staff.username)
                if (existingStaff != null) {
                    _errorMessage.value = "Username already exists"
                    _operationSuccessful.value = false
                    _isLoading.value = false
                    return@launch
                }

                repository.addStaff(staff)
                _operationSuccessful.value = true
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Failed to add staff: ${e.message}"
                _operationSuccessful.value = false
                _isLoading.value = false
            }
        }
    }

    fun updateStaff(staff: Staff) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Validate username uniqueness (except for the current staff)
                val existingStaff = repository.getStaffByUsername(staff.username)
                if (existingStaff != null && existingStaff.staffId != staff.staffId) {
                    _errorMessage.value = "Username already exists"
                    _operationSuccessful.value = false
                    _isLoading.value = false
                    return@launch
                }

                repository.updateStaff(staff)
                _operationSuccessful.value = true
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update staff: ${e.message}"
                _operationSuccessful.value = false
                _isLoading.value = false
            }
        }
    }

    fun deactivateStaff(staffId: Long) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                repository.deactivateStaff(staffId)
                _operationSuccessful.value = true
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Failed to deactivate staff: ${e.message}"
                _operationSuccessful.value = false
                _isLoading.value = false
            }
        }
    }

    fun login(username: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val staff = repository.login(username, password)
                if (staff != null) {
                    _selectedStaff.value = staff
                    _operationSuccessful.value = true
                } else {
                    _errorMessage.value = "Invalid username or password"
                    _operationSuccessful.value = false
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Login failed: ${e.message}"
                _operationSuccessful.value = false
                _isLoading.value = false
            }
        }
    }
}