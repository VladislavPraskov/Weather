package com.example.weather.data.db.current_weather

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "current_weather_entity")
data class CurrentWeatherEntity(
    @PrimaryKey
    var id: Int = 0,
    val city: String,
    val temp: Float = 0f,
    var maxTemp: Float = 0f,
    var minTemp: Float = 0f,
    var sunrise: Long = 0,
    var sunset: Long = 0,
    val feelsLike: Float? = null,
    val humidity: Long? = null,
    val windSpeed: Int? = null,
    val pressure: Long? = null,
    val visibility: Long? = null,
    val condition: String? = null,
    val iconId: Int,
    val dewPoint: Double? = null
) : Parcelable