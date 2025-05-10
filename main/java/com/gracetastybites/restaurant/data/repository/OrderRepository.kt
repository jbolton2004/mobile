package com.gracetastybites.restaurant.data.repository

import androidx.lifecycle.LiveData
import com.gracetastybites.restaurant.data.local.dao.OrderDao
import com.gracetastybites.restaurant.data.local.dao.OrderItemDao
import com.gracetastybites.restaurant.data.local.entity.Order
import com.gracetastybites.restaurant.data.local.entity.OrderItem
import com.gracetastybites.restaurant.data.local.entity.OrderStatus
import java.util.Date

class OrderRepository(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao
) {

    val allOrders: LiveData<List<Order>> = orderDao.getAllOrders()

    fun getOrdersByStatus(status: OrderStatus): LiveData<List<Order>> {
        return orderDao.getOrdersByStatus(status)
    }

    fun getOrdersByDateRange(startDate: Date, endDate: Date): LiveData<List<Order>> {
        return orderDao.getOrdersByDateRange(startDate, endDate)
    }

    fun getOrderById(orderId: Long): LiveData<Order> {
        return orderDao.getOrderById(orderId)
    }

    fun getOrderItemsByOrderId(orderId: Long): LiveData<List<OrderItem>> {
        return orderItemDao.getOrderItemsByOrderId(orderId)
    }

    suspend fun createOrder(order: Order, orderItems: List<OrderItem>): Long {
        val orderId = orderDao.insert(order)

        // Update order ID in order items
        val updatedOrderItems = orderItems.map { it.copy(orderId = orderId) }
        orderItemDao.insertAll(updatedOrderItems)

        return orderId
    }

    suspend fun updateOrder(order: Order) {
        orderDao.update(order)
    }

    suspend fun updateOrderStatus(orderId: Long, status: OrderStatus) {
        orderDao.updateOrderStatus(orderId, status)
    }

    suspend fun addOrderItem(orderItem: OrderItem) {
        orderItemDao.insert(orderItem)
    }

    suspend fun updateOrderItem(orderItem: OrderItem) {
        orderItemDao.update(orderItem)
    }

    suspend fun removeOrderItem(orderItem: OrderItem) {
        orderItemDao.delete(orderItem)
    }
}