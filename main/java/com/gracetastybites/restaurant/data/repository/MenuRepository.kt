package com.gracetastybites.restaurant.data.repository

import androidx.lifecycle.LiveData
import com.gracetastybites.restaurant.data.local.dao.MenuDao
import com.gracetastybites.restaurant.data.local.entity.MenuItem
import com.gracetastybites.restaurant.data.local.entity.MenuCategory

class MenuRepository(private val menuDao: MenuDao) {

    val allAvailableMenuItems: LiveData<List<MenuItem>> = menuDao.getAllAvailableMenuItems()
    val allMenuItems: LiveData<List<MenuItem>> = menuDao.getAllMenuItems()

    fun getMenuItemsByCategory(category: MenuCategory): LiveData<List<MenuItem>> {
        return menuDao.getMenuItemsByCategory(category)
    }

    fun getMenuItemById(itemId: Long): LiveData<MenuItem> {
        return menuDao.getMenuItemById(itemId)
    }

    suspend fun addMenuItem(menuItem: MenuItem): Long {
        return menuDao.insert(menuItem)
    }

    suspend fun updateMenuItem(menuItem: MenuItem) {
        menuDao.update(menuItem)
    }

    suspend fun updateAvailability(itemId: Long, isAvailable: Boolean) {
        menuDao.updateAvailability(itemId, isAvailable)
    }

    suspend fun deleteMenuItem(menuItem: MenuItem) {
        menuDao.delete(menuItem)
    }
}