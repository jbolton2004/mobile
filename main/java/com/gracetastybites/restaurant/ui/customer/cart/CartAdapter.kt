package com.gracetastybites.restaurant.ui.customer.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gracetastybites.restaurant.R
import com.gracetastybites.restaurant.data.local.entity.MenuItem

class CartAdapter(
    private val onIncreaseQuantity: (MenuItem) -> Unit,
    private val onDecreaseQuantity: (MenuItem) -> Unit,
    private val onRemoveItem: (MenuItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var cartItems: List<Pair<MenuItem, Int>> = emptyList()

    fun submitList(items: List<Pair<MenuItem, Int>>) {
        cartItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val (menuItem, quantity) = cartItems[position]
        holder.bind(menuItem, quantity)
    }

    override fun getItemCount(): Int = cartItems.size

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textItemName: TextView = itemView.findViewById(R.id.textItemName)
        private val textItemPrice: TextView = itemView.findViewById(R.id.textItemPrice)
        private val textQuantity: TextView = itemView.findViewById(R.id.textQuantity)
        private val textItemTotal: TextView = itemView.findViewById(R.id.textItemTotal)
        private val buttonIncrease: ImageButton = itemView.findViewById(R.id.buttonIncrease)
        private val buttonDecrease: ImageButton = itemView.findViewById(R.id.buttonDecrease)
        private val buttonRemove: Button = itemView.findViewById(R.id.buttonRemove)

        fun bind(menuItem: MenuItem, quantity: Int) {
            textItemName.text = menuItem.name
            textItemPrice.text = String.format("$%.2f", menuItem.price)
            textQuantity.text = quantity.toString()
            textItemTotal.text = String.format("$%.2f", menuItem.price * quantity)

            buttonIncrease.setOnClickListener {
                onIncreaseQuantity(menuItem)
            }

            buttonDecrease.setOnClickListener {
                onDecreaseQuantity(menuItem)
            }

            buttonRemove.setOnClickListener {
                onRemoveItem(menuItem)
            }
        }
    }
}