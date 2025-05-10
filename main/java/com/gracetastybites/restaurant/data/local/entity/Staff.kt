package com.gracetastybites.restaurant.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class StaffRole {
    CHEF, WAITER, CASHIER, MANAGER, ADMIN
}

@Entity(tableName = "staff")
data class Staff(
    @PrimaryKey(autoGenerate = true)
    val staffId: Long = 0,
    val name: String,
    val email: String,
    val phone: String,
    val role: StaffRole,
    val payPerShift: Double,
    val username: String,
    val password: String, // In a real app, this should be hashed
    val isActive: Boolean = true
)