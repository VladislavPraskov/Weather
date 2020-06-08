package com.example.weather.data.db.city

import androidx.room.Entity

@Entity(tableName = "city_entity", primaryKeys = ["idString"])
data class CityEntity(
    val idString: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val cityName: String = "",
    val utc: String = "",
    val country: String = "",
    val offsetSec: Long = 0,
    var isCurrentSelected: Boolean = false
)