package com.example.weather.models.main

data class CurrentUI(
    val city: String,
    val temp: String = "",
    var maxTemp: String = "",
    var minTemp: String = "",
    var sunrise: Long = 0,
    var sunset: Long = 0,
    val feelsLike: Float? = null,
    val humidity: Long? = null,
    val windSpeed: Int? = null,
    val pressure: Long? = null,
    val visibility: Long? = null,
    val condition: String? = null,
    val iconId: Int,
    val dewPoint: Double? = null
)