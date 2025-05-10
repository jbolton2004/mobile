package com.gracetastybites.restaurant.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val orderId: Long = 0,
    val customerName: String,
    val customerPhone: String? = null,
    val orderDate: Date,
    val totalAmount: Double,
    val isPaid: Boolean = false,
    val status: OrderStatus = OrderStatus.PENDING
)

enum class OrderStatus {
    PENDING, PREPARING, READY, DELIVERED, CANCELLED
}