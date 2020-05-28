package com.example.weather.models.main

data class DayUI(
    val time: Long,
    var maxTemp: Float = 0f,
    var minTemp: Float = 0f,
    val dayOfWeek: String,
    val iconId: Int
)