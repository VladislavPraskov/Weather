package com.example.weather.models.main

import android.os.Parcelable
import com.example.weather.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrentUI(
    val city: String = "City",
    val temp: String = "0",
    val time: Float = 0f,
    val timeOffset: Int = 0,
    var maxTemp: String = "min",
    var minTemp: String = "max",
    var sunrise: Float = 0f,
    var sunset: Float = 0f,
    var sunriseH: String = "",
    var sunsetH: String = "",
    var sunDayH: String = "",
    val feelsLike: String = "",
    val humidity: String = "null",
    val windSpeed: String = "",
    val pressure: String = "",
    val visibility: String = "",
    val condition: String? = null,
    val iconId: Int = R.drawable.cloud_icon,
    val colorStartId: Int = 0x6200EE,
    val colorEndId: Int = 0x6200EE,
    val dewPoint: String = ""
) : Parcelable