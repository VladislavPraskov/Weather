package com.example.weather.models

data class CityUI(
    val cityName: String,
    val utc: String,
    val country: String,
    val offsetSec: Long,
    var isCache: Boolean = false,
    val lg: Double,
    val lt: Double,
    var isCurrentSelected: Boolean = false,
    val idString: String
)