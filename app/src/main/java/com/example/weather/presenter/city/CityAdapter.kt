package com.example.weather.presenter.city

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.devpraskov.android_ext.onClick
import com.example.weather.R
import com.example.weather.models.CityUI
import kotlinx.android.synthetic.main.city_item.view.*

class CityAdapter(private val click: (CityUI) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CityUI>() {

        override fun areItemsTheSame(oldItem: CityUI, newItem: CityUI): Boolean {
            return oldItem.geonameId == newItem.geonameId
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
            itemView.onClick {
                click.invoke(item)
            }
        }
    }

}