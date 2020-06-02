package com.example.weather.presenter.city

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


class CityItemTouchHelper(delete: (Int) -> Unit) : ItemTouchHelper(
    object : ItemTouchHelper.SimpleCallback(0, LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: ViewHolder, target: ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
            delete.invoke(viewHolder.adapterPosition)
        }
    })