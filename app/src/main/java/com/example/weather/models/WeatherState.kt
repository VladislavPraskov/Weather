package com.example.weather.models

import android.content.Context
import android.graphics.Color
import com.devpraskov.android_ext.getColorCompat
import com.example.weather.R

data class WeatherState(
    val colorStartId: Int,
    val colorEndId: Int,
    val iconId: Int
) {
    companion object {
        fun create(
            weatherId: Int,
            sunrise: Long? = null,
            sunset: Long? = null,
            iconId: String
        ): WeatherState {

            val isDay = iconId.contains("d")
            return when (weatherId) {
                in 200..232 -> {
                    val color = if (isDay) thunderDay.random() else thunderNight.random()
                    WeatherState(color.first, color.second, R.drawable.thunder)
                }
                in 300..321, 520 - 531 -> {
                    val color = if (isDay) drizzleDay.random() else drizzleNight.random()
                    WeatherState(color.first, color.second, R.drawable.rain_icon)
                }
                in 500..504 -> {
                    val color = if (isDay) rainSun.random() else drizzleNight.random()
                    WeatherState(
                        color.first,
                        color.second,
                        R.drawable.cloud_sun_rain_icon
                    ) //todo night
                }
                511 -> {
                    val color = rainSnow.random()
                    WeatherState(color.first, color.second, R.drawable.snow_icon)
                }
                in 600..622 -> {
                    val color = snow.random()
                    WeatherState(color.first, color.second, R.drawable.snowflake_icon)
                }
                in 701..721, 741 -> {
                    val color = fog.random()
                    WeatherState(color.first, color.second, R.drawable.fog_icon)
                }
                731, in 751..762 -> {
                    val color = sandstorm.random()
                    WeatherState(color.first, color.second, R.drawable.sandstorm_icon)
                }
                771 -> {
                    val color = fog.random()//todo
                    WeatherState(color.first, color.second, R.drawable.wind_icon)
                }
                781 -> {
                    val color = tornado.random()
                    WeatherState(color.first, color.second, R.drawable.tornado_icon)
                }
                800 -> {
                    val color = if (isDay) clearDay.random() else clearNight.random()
                    val icon = if (isDay) R.drawable.sun_icon else R.drawable.moon_icon
                    WeatherState(color.first, color.second, icon)
                }
                801 -> {
                    val color = if (isDay) rainSun.random() else drizzleNight.random()
                    val icon = R.drawable.cloud_sun_rain_icon //todo night
                    WeatherState(color.first, color.second, icon)
                }
                802 -> {
                    val color = cloudsLight.random()
                    val icon = R.drawable.cloud_icon //todo night
                    WeatherState(color.first, color.second, icon)
                }
                803, 804 -> {
                    val color = cloudsHeavy.random()
                    val icon = R.drawable.two_clouds_icon
                    WeatherState(color.first, color.second, icon)
                }
                else -> {
                    val color = cloudsLight.random()
                    val icon = R.drawable.cloud_icon //todo night
                    WeatherState(color.first, color.second, icon)
                }
            }
        }
    }
}

