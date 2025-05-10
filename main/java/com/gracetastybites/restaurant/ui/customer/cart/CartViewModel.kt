package com.gracetastybites.restaurant.ui.customer.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gracetastybites.restaurant.data.local.entity.MenuItem
import com.gracetastybites.restaurant.data.local.entity.Order
import com.gracetastybites.restaurant.data.local.entity.OrderItem
import com.gracetastybites.restaurant.data.repository.OrderRepository
import kotlinx.coroutines.launch
import java.util.*

class CartViewModel(private val orderRepository: OrderRepository) : ViewModel() {

    // Map of MenuItem to quantity
    private val _cartItems = MutableLiveData<MutableMap<MenuItem, Int>>(mutableMapOf())
    val cartItems: LiveData<MutableMap<MenuItem, Int>> = _cartItems

    // Total amount in cart
    private val _totalAmount = MutableLiveData(0.0)
    val totalAmount: LiveData<Double> = _totalAmount

    // Order success status
    private val _orderPlaced = MutableLiveData(false)
    val orderPlaced: LiveData<Boolean> = _orderPlaced

    // New order ID after placing order
    private val _newOrderId = MutableLiveData<Long>(0)
    val newOrderId: LiveData<Long> = _newOrderId

    // Error message
    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    // Add item to cart
    fun addToCart(menuItem: MenuItem, quantity: Int = 1) {
        val currentCart = _cartItems.value ?: mutableMapOf()
        val currentQuantity = currentCart[menuItem] ?: 0
        currentCart[menuItem] = currentQuantity + quantity
        _cartItems.value = currentCart
        calculateTotal()
    }

    // Remove item from cart
    fun removeFromCart(menuItem: MenuItem) {
        val currentCart = _cartItems.value ?: mutableMapOf()
        currentCart.remove(menuItem)
        _cartItems.value = currentCart
        calculateTotal()
    }

    // Update item quantity
    fun updateQuantity(menuItem: MenuItem, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(menuItem)
            return
        }

        val currentCart = _cartItems.value ?: mutableMapOf()
        currentCart[menuItem] = quantity
        _cartItems.value = currentCart
        calculateTotal()
    }

    // Clear cart
    fun clearCart() {
        _cartItems.value = mutableMapOf()
        _totalAmount.value = 0.0
    }

    // Calculate total amount
    private fun calculateTotal() {
        val total = _cartItems.value?.entries?.sumOf { (item, quantity) ->
            item.price * quantity
        } ?: 0.0
        _totalAmount.value = total
    }

    // Place order
    fun placeOrder(customerName: String, customerPhone: String? = null) {
        viewModelScope.launch {
            try {
                val cartItems = _cartItems.value
                if (cartItems.isNullOrEmpty()) {
                    _errorMessage.value = "Your cart is empty"
                    return@launch
                }

                // Create order
                val order = Order(
                    customerName = customerName,
                    customerPhone = customerPhone,
                    orderDate = Date(),
                    totalAmount = _totalAmount.value ?: 0.0
                )

                // Create order items
                val orderItems = cartItems.map { (menuItem, quantity) ->
                    OrderItem(
                        orderId = 0, // Will be updated by repository
                        itemId = menuItem.itemId,
                        quantity = quantity,
                        priceAtOrder = menuItem.price
                    )
                }

                // Save to database
                val orderId = orderRepository.createOrder(order, orderItems)
                _newOrderId.value = orderId
                _orderPlaced.value = true

                // Clear cart after successful order
                clearCart()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to place order: ${e.message}"
            }
        }
    }
}