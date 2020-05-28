package com.example.weather.models.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrentUI(
    val city: String,
    val temp: String = "",
    var maxTemp: String = "",
    var minTemp: String = "",
    var sunrise: Long = 0,
    var sunset: Long = 0,
    val feelsLike: String = "",
    val humidity: String = "null",
    val windSpeed: String = "",
    val pressure: String = "",
    val visibility: String = "",
    val condition: String? = null,
    val iconId: Int,
    val dewPoint: String = ""
) : Parcelable