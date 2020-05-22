package com.example.weather.data.db.city

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(indices = [Index(value = ["name"], unique = true)])
data class City(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val lat: Double? = null,
    val lon: Double? = null,
    val name: String? = null,
    var isCurrentSelected: Boolean = false
) {
    companion object {
        fun create(responseCity: HourlyWeatherForecast.City?): City {
            return City(
                id = responseCity?.id ?: Random.nextLong(),
                lat = responseCity?.coord?.lat,
                lon = responseCity?.coord?.lon,
                name = responseCity?.name
            )
        }
    }
}