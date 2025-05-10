package com.gracetastybites.restaurant.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MenuCategory {
    APPETIZER, MAIN_COURSE, DESSERT, BEVERAGE
}

@Entity(tableName = "menu_items")
data class MenuItem(
    @PrimaryKey(autoGenerate = true)
    val itemId: Long = 0,
    val name: String,
    val description: String,
    val price: Double,
    val category: MenuCategory,
    val imageUrl: String? = null, // For storing image path
    val isAvailable: Boolean = true
)