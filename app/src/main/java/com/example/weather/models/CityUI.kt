package com.example.weather.models

data class CityUI(
    val cityName: String = "",
    val utc: String = "",
    val country: String = "",
    val offsetSec: Long = 0L,
    var isCache: Boolean = false,
    val lg: Double = 0.0,
    val lt: Double = 0.0,
    var isCurrentSelected: Boolean = false,
    val idString: String = ""
)