package com.example.weather.presenter.city

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.postDelayed
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.devpraskov.android_ext.onClick
import com.devpraskov.android_ext.show
import com.example.weather.R
import com.example.weather.models.CityUI
import kotlinx.android.synthetic.main.city_item.view.*

class CityAdapter(private val click: (CityUI) -> Unit) :
    ListAdapter<CityUI, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CityUI>() {
            override fun areItemsTheSame(oldItem: CityUI, newItem: CityUI): Boolean {
                return oldItem.countryAndPostCode == newItem.countryAndPostCode
            }

            override fun areContentsTheSame(oldItem: CityUI, newItem: CityUI): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CityHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.city_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CityHolder -> {
                holder.bind(currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun submitList(list: List<CityUI>?) {
        list ?: return
        super.submitList(list)
    }

    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: CityUI) = with(itemView) {
            cityName?.text = item.cityName
            gmt.text = item.utc
            locality.text = item.country
            if (item.isCurrentSelected) {
                checked.show()
                animateChecked(checked?.drawable)
            } else checked.setImageDrawable(context.getDrawable(R.drawable.checked_animation))
            itemView.onClick {
                if (item.isCurrentSelected) return@onClick
                item.isCurrentSelected = true
                animateChecked(checked?.drawable)
                checked?.postDelayed(300) { click.invoke(item) } //Чтобы ВСЕ увидели анимацию галочки!
            }
        }
    }

    fun animateChecked(d: Drawable?) {
        if (d is AnimatedVectorDrawableCompat) d.start()
        else if (d is AnimatedVectorDrawable) d.start()

    }

    fun deleteItem(pos: Int): CityUI {
        val deletedItem = currentList[pos]
        val new = currentList.toMutableList()
        new.removeAt(pos)
        submitList(new)
        return deletedItem
    }
}