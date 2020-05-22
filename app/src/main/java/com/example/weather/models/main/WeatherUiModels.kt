package com.example.weather.models.main

import com.example.weather.data.db.weather.WeatherEntity

data class WeatherUiModels(
    val city: String,
    val temp: Int,
    val maxTemp: Int,
    val minTemp: Int,
    val date: String,
    val mainIcon: Int,
    val condition: String,
    val tempList: List<Pair<Int, Int>> //temp, icon_res
) {

    companion object {
        fun create(resultObj: List<WeatherEntity>): WeatherUiModels {
            val first = resultObj.getOrNull(0)
            val currentTemp =
                WeatherUiModels(
                    city = first?.city ?: "-",
                    temp = first?.temp?.toInt() ?: 0,
                    maxTemp = first?.maxTemp?.toInt() ?: 0,
                    minTemp = first?.minTemp?.toInt() ?: 0,
                    date = resultObj,
                    tempList = resultObj,
                    tempList = resultObj,

                    )
        }
    }
}