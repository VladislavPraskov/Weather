package com.example.weather.data.db.weather

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weather.models.main.ForecastType
import com.example.weather.models.main.HourlyWeather

@Entity
data class HourlyWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val locality: String,
    val feelsLike: Float,
    val humidity: Int,
    val windSpeed: Int,
    val pressure: Int,
    val visibility: Int,
    val dewPoint: Int,
    @ForecastType
    val type: Int
) {
}