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

        override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {
            if (viewHolder.itemView.tag == DISABLE_SWIPE_TAG) return 0
            return super.getSwipeDirs(recyclerView, viewHolder)
        }

        override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
            delete.invoke(viewHolder.adapterPosition)
        }
    }) {
    companion object {
        const val DISABLE_SWIPE_TAG = "disable_swipe_tag"
    }
}