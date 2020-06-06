package com.example.weather.models

import com.example.weather.R

data class WeatherState(
    val colorStart: String,
    val colorEnd: String,
    val iconId: Int
) {
    companion object {
        fun create(weatherId: Int, sunrise: Long, sunset: Long, iconId: String): WeatherState {
            val isDay = iconId.contains("d")
             when (weatherId) {
                in 200..232 -> {
                    val color = if (isDay) thunderDay.random() else thunderNight.random()
                    WeatherState(color.first, color.second, R.drawable.thunder)
                }
                in 300..321, 520 - 531 -> {
                    val color = if (isDay) drizzleDay.random() else drizzleNight.random()
                    WeatherState(color.first, color.second, R.drawable.rain_icon)
                }
                in 500..504 -> {
                    val color = if (isDay) rainSun.random() else drizzleNight.random()
                    WeatherState(color.first, color.second, R.drawable.rain_icon)
                }
                511 -> {
                    val color = if (isDay) rainSun.random() else drizzleNight.random()
                    WeatherState(color.first, color.second, R.drawable.snow_icon)
                }
                in 600..622 -> {
                    val color = if (isDay) rainSun.random() else drizzleNight.random()
                    WeatherState(color.first, color.second, R.drawable.snow_icon)
                }
                in 701..721, 741 -> {
                }
                731, 751 -> {
                }
                771 -> {
                }
                781 -> {
                }
                800 -> {
                }
                801 -> {
                }
                802 -> {
                }
                803 -> {
                }
                804 -> {
                }
            }
            val color = if (isDay) thunderDay.random() else thunderNight.random()
            return WeatherState(color.first, color.second, R.drawable.snow_icon)
        }
    }
}

