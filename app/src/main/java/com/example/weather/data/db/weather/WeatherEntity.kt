package com.example.weather.data.db.weather

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weather.models.main.ForecastType
import com.example.weather.models.main.HOURLY
import com.example.weather.models.main.HourlyWeather

@Entity(tableName = "weather_entity")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val city: String,
    val currentTime: Long,
    val temp: Float = 0f,
    val maxTemp: Float = 0f,
    val minTemp: Float = 0f,
    val feelsLike: Float,
    val humidity: Int,
    val windSpeed: Int,
    val pressure: Int,
    val visibility: Int,
    val dewPoint: Int,
    @ForecastType
    val type: Int
) {
    companion object {
        fun createHourlyWeather(hourly: HourlyWeather.Hourly?): WeatherEntity? {
            hourly ?: return null
            return WeatherEntity(
                city = hourly.temp,
                temp = hourly.temp?.toFloat() ?: 0f,
                type = HOURLY,
                currentTime = hourly.dt ?: System.currentTimeMillis() / 1000
            )
        }
    }
}