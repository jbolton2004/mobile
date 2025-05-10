package com.gracetastybites.restaurant.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.gracetastybites.restaurant.R

class OrdersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // For now, we'll just add a temporary TextView if the layout doesn't exist
        // This is just temporary so navigation works
        if (view is ViewGroup && view.childCount == 0) {
            val textView = TextView(requireContext())
            textView.text = "Orders Coming Soon"
            textView.textSize = 24f
            textView.setPadding(50, 50, 50, 50)
            view.addView(textView)
        }
    }
}