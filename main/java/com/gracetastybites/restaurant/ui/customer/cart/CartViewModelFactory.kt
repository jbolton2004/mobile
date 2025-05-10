package com.gracetastybites.restaurant.ui.customer.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gracetastybites.restaurant.data.repository.OrderRepository

class CartViewModelFactory(private val orderRepository: OrderRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            return CartViewModel(orderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}