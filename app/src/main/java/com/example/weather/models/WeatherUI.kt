package com.example.weather.models

import com.example.weather.models.main.CurrentUI
import com.example.weather.models.main.DayUI
import com.example.weather.models.main.HourUI

data class WeatherUI(
    val current: CurrentUI,
    val days: List<DayUI>,
    val hours: List<HourUI>
)
