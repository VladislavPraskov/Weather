package com.example.weather.data.db.hour

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "hour_entity", indices = [Index(value = ["city", "time"], unique = true)])
data class HourEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val city: String,
    val time: Long,
    val timeOffset: Int,
    val temp: Float = 0f,
    val timeDebug: String,
    val iconId: Int
)