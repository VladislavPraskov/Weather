package com.example.weather.data.db.weather

import androidx.room.*
import com.devpraskov.android_ext.beginOfDay
import com.devpraskov.android_ext.beginOfHour
import com.devpraskov.android_ext.endOfDay
import com.example.weather.models.main.DAILY
import com.example.weather.models.main.ForecastType
import com.example.weather.models.main.HOURLY

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveListWeather(list: List<WeatherEntity>)

    @Query("SELECT * FROM weather_entity WHERE city = :cityName AND time > :dateFrom AND time <= :dateTo AND type = :type ORDER BY time LIMIT 6")
    fun getHourlyForecast(
        cityName: String,
        dateFrom: Long = beginOfHour(),
        dateTo: Long = beginOfHour() + 6 * 3600, // берём 5 часов
        @ForecastType type: Int = HOURLY
    ): List<WeatherEntity>

    @Query("SELECT * FROM weather_entity WHERE city = :cityName AND time > :dateFrom AND time <= :dateTo AND type = :type  ORDER BY time LIMIT 1")
    fun getCurrentDay(
        cityName: String,
        dateFrom: Long = beginOfDay(),
        dateTo: Long = endOfDay(),
        @ForecastType type: Int = DAILY
    ): WeatherEntity


    @Transaction
    fun getCurrentDayAndHourlyForecast(cityName: String): List<WeatherEntity> {
        val day = getCurrentDay(cityName)
        val hourlyList = getHourlyForecast(cityName)
        val result = mutableListOf(day)
        result.addAll(hourlyList)
        return result
    }
}