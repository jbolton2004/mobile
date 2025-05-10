package com.gracetastybites.restaurant.ui.customer.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gracetastybites.restaurant.data.local.entity.MenuItem
import com.gracetastybites.restaurant.data.local.entity.MenuCategory
import com.gracetastybites.restaurant.data.repository.MenuRepository
import kotlinx.coroutines.launch

class MenuViewModel(private val repository: MenuRepository) : ViewModel() {

    private val _menuItems = MutableLiveData<List<MenuItem>>()
    val menuItems: LiveData<List<MenuItem>> = _menuItems

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        loadAllMenuItems()
    }

    fun loadAllMenuItems() {
        _isLoading.value = true
        repository.allAvailableMenuItems.observeForever { items ->
            _menuItems.value = items
            _isLoading.value = false
        }
    }

    fun loadAllMenuItemsAdmin() {
        _isLoading.value = true
        repository.allMenuItems.observeForever { items ->
            _menuItems.value = items
            _isLoading.value = false
        }
    }

    fun loadMenuItemsByCategory(category: MenuCategory) {
        _isLoading.value = true
        repository.getMenuItemsByCategory(category).observeForever { items ->
            _menuItems.value = items
            _isLoading.value = false
        }
    }

    fun updateMenuItemAvailability(itemId: Long, isAvailable: Boolean) {
        viewModelScope.launch {
            try {
                repository.updateAvailability(itemId, isAvailable)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update item availability: ${e.message}"
            }
        }
    }

    fun addMenuItem(menuItem: MenuItem, onSuccess: (Long) -> Unit) {
        viewModelScope.launch {
            try {
                val newItemId = repository.addMenuItem(menuItem)
                onSuccess(newItemId)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to add menu item: ${e.message}"
            }
        }
    }

    fun updateMenuItem(menuItem: MenuItem) {
        viewModelScope.launch {
            try {
                repository.updateMenuItem(menuItem)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update menu item: ${e.message}"
            }
        }
    }

    fun deleteMenuItem(menuItem: MenuItem) {
        viewModelScope.launch {
            try {
                repository.deleteMenuItem(menuItem)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete menu item: ${e.message}"
            }
        }
    }
}