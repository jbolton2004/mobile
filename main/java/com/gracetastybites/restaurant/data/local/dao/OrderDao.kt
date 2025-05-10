package com.gracetastybites.restaurant.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gracetastybites.restaurant.data.local.entity.Order
import com.gracetastybites.restaurant.data.local.entity.OrderStatus
import java.util.Date

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    fun getAllOrders(): LiveData<List<Order>>

    @Query("SELECT * FROM orders WHERE status = :status ORDER BY orderDate DESC")
    fun getOrdersByStatus(status: OrderStatus): LiveData<List<Order>>

    @Query("SELECT * FROM orders WHERE orderDate BETWEEN :startDate AND :endDate ORDER BY orderDate DESC")
    fun getOrdersByDateRange(startDate: Date, endDate: Date): LiveData<List<Order>>

    @Query("SELECT * FROM orders WHERE orderId = :orderId LIMIT 1")
    fun getOrderById(orderId: Long): LiveData<Order>

    @Insert
    suspend fun insert(order: Order): Long

    @Update
    suspend fun update(order: Order)

    @Query("UPDATE orders SET status = :status WHERE orderId = :orderId")
    suspend fun updateOrderStatus(orderId: Long, status: OrderStatus)
}