package com.example.weather.data.db

import androidx.room.*
import com.devpraskov.android_ext.beginOfDay
import com.devpraskov.android_ext.endOfDay
import com.example.weather.data.db.current_weather.CurrentWeatherEntity
import com.example.weather.data.db.day.DayEntity
import com.example.weather.data.db.hour.HourEntity
import com.example.weather.models.WeatherUI
import com.example.weather.utils.*

@Dao
abstract class SharedDao(val db: WeatherDataBase) {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveHourlyWeather(hours: List<HourEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveDailyWeather(it: List<DayEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveCurrentWeather(it: CurrentWeatherEntity)

    @Query("SELECT * FROM current_weather_entity WHERE city =:cityName LIMIT 1")
    abstract fun getCurrentWeather(
        cityName: String
    ): CurrentWeatherEntity

    @Query("SELECT * FROM hour_entity WHERE city = :cityName AND time > :dateFrom AND time <= :dateTo LIMIT 6")
    abstract fun getHourlyWeatherForecast(
        cityName: String,
        dateFrom: Long = beginOfDay() - 1,
        dateTo: Long = endOfDay() + 1
    ): List<HourEntity>

    @Query("SELECT * FROM day_entity WHERE city = :cityName AND time > :dateFrom LIMIT 7")
    abstract fun getDailyWeatherForecast(
        cityName: String,
        dateFrom: Long = beginOfDay() + 24 * 3600
    ): List<DayEntity>

    @Transaction
    open fun getWeather(): WeatherUI? {
        val city = db.cityDao.getCurrentCity()?.name ?: return null
        return WeatherUI(
            current = mapToCurrentUI(getCurrentWeather(city)),
            hours = getHourlyWeatherForecast(city).map { mapToHourUI(it) },
            days = getDailyWeatherForecast(city).map { mapToDayUI(it) }
        )
    }

}