package com.example.weather.models.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrentUI(
    val city: String,
    val temp: String = "",
    val time: Float = 0f,
    var maxTemp: String = "",
    var minTemp: String = "",
    var sunrise: Float = 0f,
    var sunset: Float = 0f,
    val feelsLike: String = "",
    val humidity: String = "null",
    val windSpeed: String = "",
    val pressure: String = "",
    val visibility: String = "",
    val condition: String? = null,
    val iconId: Int,
    val dewPoint: String = ""
) : Parcelable