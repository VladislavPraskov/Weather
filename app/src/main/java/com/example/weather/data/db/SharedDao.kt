package com.example.weather.data.db

import androidx.room.*
import com.devpraskov.android_ext.beginOfHour
import com.devpraskov.android_ext.endOfDay
import com.example.weather.data.db.current_weather.CurrentWeatherEntity
import com.example.weather.data.db.day.DayEntity
import com.example.weather.data.db.hour.HourEntity
import com.example.weather.models.WeatherUI
import com.example.weather.models.main.HourlyWeather
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

    @Query("SELECT * FROM hour_entity WHERE city = :cityName AND time > :dateFrom LIMIT 6")
    abstract fun getHourlyWeatherForecast(
        cityName: String,
        dateFrom: Long = beginOfHour() - 1
    ): List<HourEntity>

    @Query("SELECT * FROM day_entity WHERE city = :cityName AND time > :dateFrom LIMIT 7")
    abstract fun getDailyWeatherForecast(
        cityName: String,
        dateFrom: Long = endOfDay() + 1
    ): List<DayEntity>

    @Transaction
    open fun getWeather(): WeatherUI? {
        val city = db.cityDao.getCurrentCity()?.cityName ?: return null
        val current = mapToCurrentUI(getCurrentWeather(city))
        val hours = getHourlyWeatherForecast(city).also {
            it.getOrNull(0)?.iconId = current.iconId
        }.map { entity -> mapToHourUI(entity) }
        val days = getDailyWeatherForecast(city).map { mapToDayUI(it) }

        return WeatherUI(current = current, hours = hours, days = days)
    }

    @Transaction
    open fun saveWeather(networkObject: HourlyWeather) {
        val cityName = db.cityDao.getCurrentCityName()

        networkObject.apply {
            hourly?.mapNotNull { mapToHourEntity(it, cityName, timezoneOffset) }
                ?.let { saveHourlyWeather(it) }

            daily?.mapNotNull { mapToDayEntity(it, cityName, timezoneOffset) }
                ?.let { saveDailyWeather(it) }

            mapToCurrentEntity(current, hourly, daily?.getOrNull(0), timezoneOffset, cityName)
                ?.let { saveCurrentWeather(it) }
        }

    }

}