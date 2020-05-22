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
    val time: Long,
    val temp: Float = 0f,
    val maxTemp: Float = 0f,
    val minTemp: Float = 0f,
    val feelsLike: Float? = null,
    val humidity: Int? = null,
    val windSpeed: Int? = null,
    val pressure: Int? = null,
    val visibility: Int? = null,
    val iconId: String? = null,
    val dewPoint: Int? = null,
    @ForecastType
    val type: Int
) {
    companion object {
        fun createHourlyWeather(
            hourly: HourlyWeather.Hourly?,
            cityName: String?
        ): WeatherEntity? {
            hourly ?: return null
            return WeatherEntity(
                city = cityName ?: "-",
                temp = hourly.temp?.toFloat() ?: 0f,
                type = HOURLY,
                iconId = hourly.weather.icon
                time = hourly.dt ?: System.currentTimeMillis() / 1000
            )
        }
    }
}