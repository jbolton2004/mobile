package com.gracetastybites.restaurant.ui.customer.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gracetastybites.restaurant.data.repository.MenuRepository

class MenuViewModelFactory(private val repository: MenuRepository) : ViewModelProvider.Factory {
    // Rest of the code unchanged


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            return MenuViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}