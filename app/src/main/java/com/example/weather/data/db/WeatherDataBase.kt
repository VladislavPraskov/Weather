package com.example.weather.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weather.data.db.city.CityEntity
import com.example.weather.data.db.city.CityDao
import com.example.weather.data.db.current_weather.CurrentWeatherEntity
import com.example.weather.data.db.day.DayEntity
import com.example.weather.data.db.hour.HourEntity
import com.example.weather.data.db.weather.WeatherDao
import com.example.weather.data.db.weather.WeatherEntity


@Database(
    entities = [
        WeatherEntity::class,
        CityEntity::class,
        CurrentWeatherEntity::class,
        DayEntity::class,
        HourEntity::class
    ], version = 1
)
abstract class WeatherDataBase : RoomDatabase() {

    abstract val cityDao: CityDao
    abstract val weatherDao: WeatherDao
    abstract val sharedDao: SharedDao

    companion object {
        fun create(app: Application): WeatherDataBase {
            return Room.databaseBuilder(app, WeatherDataBase::class.java, "weather_data_base")
                .build()
        }
    }
}
