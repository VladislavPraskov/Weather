package com.example.weather.models.main

import com.example.weather.R
import kotlinx.android.synthetic.main.fragment_main.*

fun getIconRes(id: String): Int {
    return when (id) {
        "01d" -> R.drawable.sun_icon
        "02d" -> R.drawable.sun_icon
        "03d" -> R.drawable.sun_icon
        "04d" -> R.drawable.sun_icon
        "09d" -> R.drawable.sun_icon
        "10d" -> R.drawable.sun_icon
        "11d" -> R.drawable.sun_icon
        "13d" -> R.drawable.sun_icon
        "50d" -> R.drawable.sun_icon
        "01n" -> R.drawable.sun_icon
        "02n" -> R.drawable.sun_icon
        "03n" -> R.drawable.sun_icon
        "04n" -> R.drawable.sun_icon
        "09n" -> R.drawable.sun_icon
        "10n" -> R.drawable.sun_icon
        "11n" -> R.drawable.sun_icon
        "13n" -> R.drawable.sun_icon
        "50n" -> R.drawable.sun_icon
        else -> R.drawable.sun_icon
    }
}