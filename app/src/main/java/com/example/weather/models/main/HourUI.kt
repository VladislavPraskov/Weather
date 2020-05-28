package com.example.weather.models.main

data class HourUI(
    val time: Float,
    val temp: Float = 0f,
    val iconId: Int
)