package com.example.weather.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Weather::class], version = 1)
abstract class WeatherDataBase : RoomDatabase() {

    companion object {
        fun create(app: Application): WeatherDataBase {
            return Room.databaseBuilder(app, WeatherDataBase::class.java, "weather_data_base")
                .build()
        }
    }
}
