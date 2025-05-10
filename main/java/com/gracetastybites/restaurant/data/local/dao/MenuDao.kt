package com.gracetastybites.restaurant.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gracetastybites.restaurant.data.local.entity.MenuItem
import com.gracetastybites.restaurant.data.local.entity.MenuCategory

@Dao
interface MenuDao {
    @Query("SELECT * FROM menu_items WHERE isAvailable = 1")
    fun getAllAvailableMenuItems(): LiveData<List<MenuItem>>

    @Query("SELECT * FROM menu_items")
    fun getAllMenuItems(): LiveData<List<MenuItem>>

    @Query("SELECT * FROM menu_items WHERE category = :category AND isAvailable = 1")
    fun getMenuItemsByCategory(category: MenuCategory): LiveData<List<MenuItem>>

    @Query("SELECT * FROM menu_items WHERE itemId = :itemId")
    fun getMenuItemById(itemId: Long): LiveData<MenuItem>

    @Insert
    suspend fun insert(menuItem: MenuItem): Long

    @Update
    suspend fun update(menuItem: MenuItem)

    @Query("UPDATE menu_items SET isAvailable = :isAvailable WHERE itemId = :itemId")
    suspend fun updateAvailability(itemId: Long, isAvailable: Boolean)

    @Delete
    suspend fun delete(menuItem: MenuItem)
}