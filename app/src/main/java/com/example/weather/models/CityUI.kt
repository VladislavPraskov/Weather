package com.example.weather.models

data class CityUI(
    val cityName: String,
    val utc: String,
    val country: String,
    val offsetSec: Long,
    val lg: Double,
    val lt: Double,
    var isCurrentSelected: Boolean = false,
    val countryAndPostCode: String
)