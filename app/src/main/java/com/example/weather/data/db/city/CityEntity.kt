package com.example.weather.data.db.city

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.weather.models.main.WeatherForecast
import kotlin.random.Random

@Entity(tableName = "city_entity", indices = [Index(value = ["name"], unique = true)])
data class CityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val lat: Double? = null,
    val lon: Double? = null,
    val name: String? = null,
    var isCurrentSelected: Boolean = false
) {
    companion object {
        fun create(responseCity: WeatherForecast.City?): CityEntity {
            return CityEntity(
                id = responseCity?.id ?: Random.nextLong(),
                lat = responseCity?.coord?.lat,
                lon = responseCity?.coord?.lon,
                name = responseCity?.name
            )
        }
    }
}