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
        dateFrom: Long = beginOfHour() - 1,
        dateTo: Long = beginOfHour() + 6 * 3600 + 1, // берём 6 часов
        @ForecastType type: Int = HOURLY
    ): List<WeatherEntity>

    @Query("SELECT * FROM weather_entity WHERE city = :cityName AND time > :dateFrom AND time <= :dateTo AND type = :type LIMIT 1")
    fun getCurrentDay(
        cityName: String,
        dateFrom: Long = beginOfDay() - 1,
        dateTo: Long = endOfDay(withUTCOffset = false) - 1,
        @ForecastType type: Int = DAILY
    ): WeatherEntity

    //Костыль для сервака
    @Query("SELECT MIN(`temp`) FROM weather_entity WHERE city = :cityName AND time > :dateFrom AND time <= :dateTo AND type = :type  LIMIT 1")
    fun getMinTempOfDay(
        cityName: String,
        dateFrom: Long = beginOfDay() - 1,
        dateTo: Long = endOfDay(withUTCOffset = false) - 1,
        @ForecastType type: Int = HOURLY
    ): Float

    //Костыль для сервака
    @Query("SELECT MAX(`temp`) FROM weather_entity WHERE city = :cityName AND time > :dateFrom AND time <= :dateTo AND type = :type  LIMIT 1")
    fun getMaxTempOfDay(
        cityName: String,
        dateFrom: Long = beginOfDay() - 1,
        dateTo: Long = endOfDay(withUTCOffset = false) - 1,
        @ForecastType type: Int = HOURLY
    ): Float


    @Transaction
    fun getCurrentDayAndHourlyForecast(cityName: String): List<WeatherEntity> {
        val minTempOfDay = getMinTempOfDay(cityName)
        val maxTempOfDay = getMaxTempOfDay(cityName)
        val day = getCurrentDay(cityName)
        day.apply { minTemp = minTempOfDay; maxTemp = maxTempOfDay }
        val hourlyList = getHourlyForecast(cityName)
        val result = mutableListOf(day)
        result.addAll(hourlyList)
        return result
    }
}