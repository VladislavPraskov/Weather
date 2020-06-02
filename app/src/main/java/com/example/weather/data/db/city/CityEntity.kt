package com.example.weather.data.db.city

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.weather.models.main.WeatherForecast
import kotlin.random.Random

@Entity(tableName = "city_entity", primaryKeys = ["countryAndPostCode"])
data class CityEntity(
    val countryAndPostCode: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val cityName: String = "",
    val utc: String = "",
    val country: String = "",
    val offsetSec: Long = 0,
    var isCurrentSelected: Boolean = false
)