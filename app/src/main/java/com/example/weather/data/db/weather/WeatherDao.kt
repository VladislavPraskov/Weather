package com.example.weather.data.db.weather

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDao {

    @Insert
    fun saveListWeather(list: List<WeatherEntity>)

    @Query("SELECT * FROM weather_entity WHERE city = :cityName")
    fun getTodayHourlyForecast(cityName: String)
}