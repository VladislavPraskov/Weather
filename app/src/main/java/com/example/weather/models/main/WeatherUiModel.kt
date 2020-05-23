package com.example.weather.models.main

import com.devpraskov.android_ext.currentDayAndDate
import com.devpraskov.android_ext.getHour
import com.example.weather.data.db.weather.WeatherEntity
import java.util.*
import kotlin.math.roundToInt

data class WeatherUiModel(
    val city: String,
    val temp: String,
    val maxTemp: String,
    val minTemp: String,
    val mainIcon: Int? = null,
    val condition: String = "",
    val hourlyForecast: List<HourlyData> //temp, icon_res
) {
    companion object {
        fun create(resultObj: List<WeatherEntity>): WeatherUiModel {
            val first = resultObj.getOrNull(0)
            return WeatherUiModel(
                city = first?.city ?: "-",
                temp = first?.temp?.roundToInt()?.toString() ?: "",
                maxTemp = first?.maxTemp?.roundToInt()?.toString() ?: "",
                minTemp = first?.minTemp?.roundToInt()?.toString() ?: "",
                condition = first?.condition ?: "",
                mainIcon = first?.iconId,
                hourlyForecast = resultObj.mapIndexedNotNull { index, weather ->
                    if (index == 0) null
                    else HourlyData(
                        weather.temp,
                        getHour(Date(weather.time * 1000)),
                        weather.iconId
                    )
                }
            )
        }
    }
}

data class HourlyData(var temp: Float = 0f, var hour: Float = 0f, val iconRes: Int)