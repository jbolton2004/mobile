package com.gracetastybites.restaurant.ui.customer.menu

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gracetastybites.restaurant.R
import com.gracetastybites.restaurant.data.local.AppDatabase
import com.gracetastybites.restaurant.data.local.entity.MenuCategory
import com.gracetastybites.restaurant.data.local.entity.MenuItem
import com.gracetastybites.restaurant.data.repository.MenuRepository
import com.gracetastybites.restaurant.data.repository.OrderRepository
import com.gracetastybites.restaurant.ui.customer.cart.CartViewModel
import com.gracetastybites.restaurant.ui.customer.cart.CartViewModelFactory
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch
import java.util.Locale

class MenuFragment : Fragment() {

    private lateinit var viewModel: MenuViewModel
    private lateinit var menuAdapter: MenuItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        val menuDao = AppDatabase.getDatabase(requireContext()).menuDao()
        val repository = MenuRepository(menuDao)
        val factory = MenuViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MenuViewModel::class.java]

        // Setup RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        menuAdapter = MenuItemAdapter()
        recyclerView.adapter = menuAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Add sample menu items if database is empty
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Check if we have any menu items
                val itemCount = menuDao.getAllMenuItems().value?.size ?: 0

                if (itemCount == 0) {
                    // Database is empty, add sample data
                    addSampleMenuItems()

                    // Show a message
                    Toast.makeText(
                        requireContext(),
                        "Sample menu items added!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error checking database: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // Add to cart functionality
        menuAdapter.onItemClick = { menuItem ->
            // Show a dialog to add to cart with quantity selection
            val dialogView = layoutInflater.inflate(R.layout.dialog_add_to_cart, null)
            val dialogQuantity = dialogView.findViewById<TextView>(R.id.textQuantity)
            var quantity = 1
            dialogQuantity.text = quantity.toString()

            dialogView.findViewById<ImageButton>(R.id.buttonIncrease).setOnClickListener {
                quantity++
                dialogQuantity.text = quantity.toString()
            }

            dialogView.findViewById<ImageButton>(R.id.buttonDecrease).setOnClickListener {
                if (quantity > 1) {
                    quantity--
                    dialogQuantity.text = quantity.toString()
                }
            }

            val alertDialog = AlertDialog.Builder(requireContext())
                .setTitle("Add to Cart")
                .setView(dialogView)
                .setPositiveButton("Add") { _, _ ->
                    // Add to cart
                    val cartViewModel = ViewModelProvider(
                        requireActivity(),
                        CartViewModelFactory(
                            OrderRepository(
                                AppDatabase.getDatabase(requireContext()).orderDao(),
                                AppDatabase.getDatabase(requireContext()).orderItemDao()
                            )
                        )
                    ).get(CartViewModel::class.java)

                    cartViewModel.addToCart(menuItem, quantity)

                    Toast.makeText(
                        requireContext(),
                        "$quantity x ${menuItem.name} added to cart",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton("Cancel", null)
                .create()

            alertDialog.show()
        }

        // Observe LiveData
        viewModel.menuItems.observe(viewLifecycleOwner) { items ->
            if (items.isEmpty()) {
                // Show a message if no items
                Toast.makeText(
                    requireContext(),
                    "No menu items available",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                menuAdapter.submitList(items)
            }
        }

        // Setup Tabs for Menu Categories
        val tabLayout = view.findViewById<TabLayout>(R.id.categoryTabs)

        // Add tabs for all categories
        val allTab = tabLayout.newTab().setText("All")
        allTab.contentDescription = "Show all menu items"
        tabLayout.addTab(allTab)

        for (category in MenuCategory.values()) {
            val categoryText = formatCategory(category)
            val tab = tabLayout.newTab().setText(categoryText)
            tab.contentDescription = "Show $categoryText menu items"
            tabLayout.addTab(tab)
        }

        // Handle tab selection
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.loadAllMenuItems()
                    1 -> viewModel.loadMenuItemsByCategory(MenuCategory.APPETIZER)
                    2 -> viewModel.loadMenuItemsByCategory(MenuCategory.MAIN_COURSE)
                    3 -> viewModel.loadMenuItemsByCategory(MenuCategory.DESSERT)
                    4 -> viewModel.loadMenuItemsByCategory(MenuCategory.BEVERAGE)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun addSampleMenuItems() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Create sample menu items
                val sampleMenuItems = listOf(
                    MenuItem(
                        name = "Caesar Salad",
                        description = "Fresh romaine lettuce with our special Caesar dressing, croutons and parmesan cheese",
                        price = 8.99,
                        category = MenuCategory.APPETIZER
                    ),
                    MenuItem(
                        name = "Garlic Bread",
                        description = "Freshly baked bread with garlic butter and herbs",
                        price = 5.99,
                        category = MenuCategory.APPETIZER
                    ),
                    MenuItem(
                        name = "Grilled Chicken Sandwich",
                        description = "Tender grilled chicken breast with lettuce, tomato and mayo on a fresh bun",
                        price = 12.99,
                        category = MenuCategory.MAIN_COURSE
                    ),
                    MenuItem(
                        name = "Fish & Chips",
                        description = "Crispy battered fish served with golden fries and tartar sauce",
                        price = 14.99,
                        category = MenuCategory.MAIN_COURSE
                    ),
                    MenuItem(
                        name = "Spaghetti Bolognese",
                        description = "Al dente spaghetti with rich meat sauce and parmesan",
                        price = 13.99,
                        category = MenuCategory.MAIN_COURSE
                    ),
                    MenuItem(
                        name = "Chocolate Cake",
                        description = "Decadent three-layer chocolate cake with rich frosting",
                        price = 6.99,
                        category = MenuCategory.DESSERT
                    ),
                    MenuItem(
                        name = "Cheesecake",
                        description = "Creamy New York style cheesecake with berry compote",
                        price = 7.99,
                        category = MenuCategory.DESSERT
                    ),
                    MenuItem(
                        name = "Iced Tea",
                        description = "Refreshing house-brewed iced tea, sweetened or unsweetened",
                        price = 2.99,
                        category = MenuCategory.BEVERAGE
                    ),
                    MenuItem(
                        name = "Coffee",
                        description = "Freshly brewed coffee, served hot",
                        price = 3.49,
                        category = MenuCategory.BEVERAGE
                    ),
                    MenuItem(
                        name = "Soft Drink",
                        description = "Choice of cola, lemon-lime, or root beer",
                        price = 2.49,
                        category = MenuCategory.BEVERAGE
                    )
                )

                // Insert each item
                val repository = MenuRepository(AppDatabase.getDatabase(requireContext()).menuDao())
                for (item in sampleMenuItems) {
                    repository.addMenuItem(item)
                }

                // Refresh menu items
                viewModel.loadAllMenuItems()

            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error adding sample items: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun formatCategory(category: MenuCategory): String {
        return category.name.replace("_", " ").replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }
}