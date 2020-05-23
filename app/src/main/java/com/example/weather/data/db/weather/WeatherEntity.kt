package com.example.weather.data.db.weather

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.weather.models.main.*
import kotlin.math.roundToInt

@Entity(
    tableName = "weather_entity",
    indices = [Index(value = ["city", "time", "type"], unique = true)]
)
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val city: String,
    val time: Long,
    val temp: Float = 0f,
    val maxTemp: Float = 0f,
    val minTemp: Float = 0f,
    val feelsLike: Float? = null,
    val humidity: Long? = null,
    val windSpeed: Int? = null,
    val pressure: Long? = null,
    val visibility: Long? = null,
    val condition: String? = null,
    val iconId: Int,
    val dewPoint: Double? = null,
    @ForecastType
    val type: Int
) {
    companion object {
        fun createHourlyWeather(hourly: HourlyWeather.Hourly?, cityName: String?): WeatherEntity? {
            hourly ?: return null
            return WeatherEntity(
                city = cityName ?: "-",
                temp = hourly.temp?.toFloat() ?: 0f,
                type = HOURLY,
                iconId = getIconRes(hourly.weather?.getOrNull(0)?.icon),
                time = hourly.dt ?: System.currentTimeMillis() / 1000
            )
        }

        fun createDailyWeather(daily: HourlyWeather.Daily?, cityName: String?): WeatherEntity? {
            daily ?: return null
            return WeatherEntity(
                city = cityName ?: "-",
                temp = daily.temp?.day?.toFloat() ?: 0f,
                type = DAILY,
                minTemp = daily.temp?.min?.toFloat() ?: 0f,
                maxTemp = daily.temp?.max?.toFloat() ?: 0f,
                feelsLike = daily.feelsLike?.day?.toFloat() ?: 0f,
                humidity = daily.humidity,
                windSpeed = daily.windSpeed?.roundToInt(),
                pressure = daily.pressure,
                dewPoint = daily.dewPoLong,
                condition = daily.weather?.getOrNull(0)?.main,
                iconId = getIconRes(daily.weather?.getOrNull(0)?.icon),
                time = daily.dt ?: System.currentTimeMillis() / 1000
            )
        }
    }
}