package com.gracetastybites.restaurant.ui.customer.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gracetastybites.restaurant.data.local.entity.MenuItem
import com.gracetastybites.restaurant.data.local.entity.Order
import com.gracetastybites.restaurant.data.local.entity.OrderItem
import com.gracetastybites.restaurant.data.repository.MenuRepository
import com.gracetastybites.restaurant.data.repository.OrderRepository
import kotlinx.coroutines.launch
import java.util.*

class CartViewModel(
    private val orderRepository: OrderRepository,
    private val menuRepository: MenuRepository
) : ViewModel() {

    private val _cartItems = MutableLiveData<MutableMap<MenuItem, Int>>(mutableMapOf())
    val cartItems: LiveData<MutableMap<MenuItem, Int>> = _cartItems

    private val _totalAmount = MutableLiveData(0.0)
    val totalAmount: LiveData<Double> = _totalAmount

    private val _isOrderPlaced = MutableLiveData<Boolean>(false)
    val isOrderPlaced: LiveData<Boolean> = _isOrderPlaced

    private val _orderId = MutableLiveData<Long>(0L)
    val orderId: LiveData<Long> = _orderId

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun addToCart(menuItem: MenuItem, quantity: Int = 1) {
        val currentCart = _cartItems.value ?: mutableMapOf()
        val currentQuantity = currentCart[menuItem] ?: 0
        currentCart[menuItem] = currentQuantity + quantity
        _cartItems.value = currentCart
        calculateTotal()
    }

    fun removeFromCart(menuItem: MenuItem) {
        val currentCart = _cartItems.value ?: mutableMapOf()
        currentCart.remove(menuItem)
        _cartItems.value = currentCart
        calculateTotal()
    }

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

    fun clearCart() {
        _cartItems.value = mutableMapOf()
        _totalAmount.value = 0.0
    }

    private fun calculateTotal() {
        val total = _cartItems.value?.entries?.sumByDouble { (item, quantity) ->
            item.price * quantity
        } ?: 0.0
        _totalAmount.value = total
    }

    fun placeOrder(customerName: String, customerPhone: String?) {
        viewModelScope.launch {
            try {
                val cartItems = _cartItems.value
                if (cartItems.isNullOrEmpty()) {
                    _errorMessage.value = "Cart is empty"
                    return@launch
                }

                val order = Order(
                    customerName = customerName,
                    customerPhone = customerPhone,
                    orderDate = Date(),
                    totalAmount = _totalAmount.value ?: 0.0
                )

                val orderItems = cartItems.map { (menuItem, quantity) ->
                    OrderItem(
                        orderId = 0, // Will be set by repository
                        itemId = menuItem.itemId,
                        quantity = quantity,
                        priceAtOrder = menuItem.price
                    )
                }

                val orderId = orderRepository.createOrder(order, orderItems)
                _orderId.value = orderId
                _isOrderPlaced.value = true
                clearCart()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to place order: ${e.message}"
            }
        }
    }
}