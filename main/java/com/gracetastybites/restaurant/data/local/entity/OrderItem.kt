package com.gracetastybites.restaurant.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "order_items",
    primaryKeys = ["orderId", "itemId"],
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = ["orderId"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MenuItem::class,
            parentColumns = ["itemId"],
            childColumns = ["itemId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("orderId"), Index("itemId")]
)
data class OrderItem(
    val orderId: Long,
    val itemId: Long,
    val quantity: Int,
    val priceAtOrder: Double
)