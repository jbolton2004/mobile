package com.gracetastybites.restaurant.data.local.dao
import androidx.lifecycle.LiveData
import androidx.room.*
import com.gracetastybites.restaurant.data.local.entity.OrderItem


@Dao
interface OrderItemDao {
    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun getOrderItemsByOrderId(orderId: Long): LiveData<List<OrderItem>>

    @Insert
    suspend fun insert(orderItem: OrderItem)

    @Insert
    suspend fun insertAll(orderItems: List<OrderItem>)

    @Update
    suspend fun update(orderItem: OrderItem)

    @Delete
    suspend fun delete(orderItem: OrderItem)

    @Query("DELETE FROM order_items WHERE orderId = :orderId")
    suspend fun deleteAllItemsForOrder(orderId: Long)
}