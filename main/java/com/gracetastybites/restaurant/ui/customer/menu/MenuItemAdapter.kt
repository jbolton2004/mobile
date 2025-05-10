package com.gracetastybites.restaurant.ui.customer.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gracetastybites.restaurant.R
import com.gracetastybites.restaurant.data.local.entity.MenuItem

class MenuItemAdapter : ListAdapter<MenuItem, MenuItemAdapter.MenuItemViewHolder>(MenuItemDiffCallback()) {

    var onItemClick: ((MenuItem) -> Unit)? = null
    var onAvailabilityChanged: ((MenuItem, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        return MenuItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        val menuItem = getItem(position)
        holder.bind(menuItem)
    }

    inner class MenuItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val textName: TextView = itemView.findViewById(R.id.textName)
        private val textDescription: TextView = itemView.findViewById(R.id.textDescription)
        private val textPrice: TextView = itemView.findViewById(R.id.textPrice)
        private val switchAvailability: Switch? = itemView.findViewById(R.id.switchAvailability)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(getItem(position))
                }
            }

            // Only set up the switch if it exists in the layout
            switchAvailability?.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onAvailabilityChanged?.invoke(getItem(position), isChecked)
                }
            }
        }

        fun bind(menuItem: MenuItem) {
            textName.text = menuItem.name
            textDescription.text = menuItem.description
            textPrice.text = "$${String.format("%.2f", menuItem.price)}"

            // Set the availability switch if it exists
            switchAvailability?.let { switch ->
                // Set without triggering the listener
                switch.setOnCheckedChangeListener(null)
                switch.isChecked = menuItem.isAvailable
                switch.setOnCheckedChangeListener { _, isChecked ->
                    onAvailabilityChanged?.invoke(menuItem, isChecked)
                }
            }

            // Load image if available, otherwise use a placeholder
            if (menuItem.imageUrl != null) {
                // In a real app, you'd use a library like Glide or Picasso here
                imageView.setImageResource(R.drawable.ic_menu)
            } else {
                imageView.setImageResource(R.drawable.ic_menu)
            }
        }
    }

    class MenuItemDiffCallback : DiffUtil.ItemCallback<MenuItem>() {
        override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
            return oldItem == newItem
        }
    }
}