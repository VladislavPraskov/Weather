package com.example.weather.data.db.weather

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.devpraskov.android_ext.beginOfHour

@Dao
interface WeatherDao {

    @Insert
    fun saveListWeather(list: List<WeatherEntity>)

    @Query("SELECT * FROM weather_entity WHERE city = :cityName AND time > :dateFrom AND time <= :dateTo ORDER BY time LIMIT 5")
    fun getCurrentHourlyForecast(
        cityName: String,
        dateFrom: Long = beginOfHour(),
        dateTo: Long = beginOfHour() + 5 * 3600 // берём 5 часов
    ): List<WeatherEntity>
}