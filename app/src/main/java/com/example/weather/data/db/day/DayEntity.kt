package com.example.weather.data.db.day

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "day_entity", indices = [Index(value = ["city", "time"], unique = true)])
data class DayEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val city: String,
    val time: Long,
    val timeDebug: String,
    var maxTemp: Float = 0f,
    var minTemp: Float = 0f,
    val dayOfWeek: String,
    val iconId: Int
)