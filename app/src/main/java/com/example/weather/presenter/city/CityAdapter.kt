package com.example.weather.presenter.city

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.postDelayed
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.devpraskov.android_ext.hideAnimateAlpha
import com.devpraskov.android_ext.onClick
import com.devpraskov.android_ext.show
import com.example.weather.R
import com.example.weather.models.CityUI
import kotlinx.android.synthetic.main.city_item.view.*

class CityAdapter(private val click: (CityUI) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var avd1: AnimatedVectorDrawableCompat
    lateinit var avd2: AnimatedVectorDrawable
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CityUI>() {

        override fun areItemsTheSame(oldItem: CityUI, newItem: CityUI): Boolean {
            return oldItem.countryAndPostCode == newItem.countryAndPostCode
        }

        override fun areContentsTheSame(oldItem: CityUI, newItem: CityUI): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


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
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<CityUI>?) {
        list ?: return
        differ.submitList(list)
    }

    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: CityUI) = with(itemView) {
            cityName?.text = item.cityName
            gmt.text = item.utc
            locality.text = item.country
            if (item.isCurrentSelected) {
                checked.show()
                animateChecked(checked?.drawable)
            }
            itemView.onClick {
                if (item.isCurrentSelected) return@onClick
                animateChecked(checked?.drawable)
                checked?.postDelayed(500) { click.invoke(item) } //Чтобы ВСЕ увидели анимацию галочки!
            }
        }
    }

    fun animateChecked(d: Drawable?) {
        if (d is AnimatedVectorDrawableCompat) {
            avd1 = d
            avd1.start()
        } else if (d is AnimatedVectorDrawable) {
            avd2 = d
            avd2.start()
        }
    }

    fun deleteItem(pos: Int): CityUI {
        val deletedItem: CityUI
        differ.apply {
            deletedItem = currentList[pos]
            val new = currentList.toMutableList()
            new.removeAt(pos)
            submitList(new)
        }
        return deletedItem
    }
}