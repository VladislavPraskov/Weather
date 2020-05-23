package com.example.weather.models.main

import com.example.weather.R
import kotlinx.android.synthetic.main.fragment_main.*

fun getIconRes(id: String?): Int {
    return when (id) {
        "01d" -> R.drawable.sun_icon
        "02d" -> R.drawable.cloud_sun_icon
        "03d" -> R.drawable.cloud_icon
        "04d" -> R.drawable.two_clouds_icon
        "09d" -> R.drawable.rain_icon
        "10d" -> R.drawable.cloud_sun_rain_icon
        "11d" -> R.drawable.thunder
        "13d" -> R.drawable.snow_icon
        "50d" -> R.drawable.fog_icon
        "01n" -> R.drawable.sun_icon
        "02n" -> R.drawable.cloud_sun_icon
        "03n" -> R.drawable.cloud_icon
        "04n" -> R.drawable.two_clouds_icon
        "09n" -> R.drawable.rain_icon
        "10n" -> R.drawable.cloud_sun_rain_icon
        "11n" -> R.drawable.thunder
        "13n" -> R.drawable.snow_icon
        "50n" -> R.drawable.fog_icon
        else -> R.drawable.cloud_icon
    }
}