package com.example.weather.utils

import com.devpraskov.android_ext.*
import com.example.weather.data.db.current_weather.CurrentWeatherEntity
import com.example.weather.data.db.day.DayEntity
import com.example.weather.data.db.hour.HourEntity
import com.example.weather.models.main.CurrentUI
import com.example.weather.models.main.DayUI
import com.example.weather.models.main.HourUI
import com.example.weather.models.main.HourlyWeather.*
import com.example.weather.models.main.getIconRes
import java.util.*
import kotlin.math.roundToInt

fun mapToHourEntity(hourWeather: Hourly?, city: String?): HourEntity? {
    hourWeather ?: return null
    return HourEntity(
        city = city ?: "-",
        temp = hourWeather.temp?.toFloat() ?: 0f,
        iconId = getIconRes(hourWeather.weather?.getOrNull(0)?.icon),
        time = hourWeather.dt ?: 0,
        timeDebug = currentDateAndTime(Date((hourWeather.dt ?: 0) * 1000))
    )
}

fun mapToDayEntity(daily: Daily?, city: String?): DayEntity? {
    daily ?: return null
    return DayEntity(
        city = city ?: "-",
        dayOfWeek = dayOfWeek(Date((daily.dt ?: 0) * 1000)),
        minTemp = daily.temp?.min?.toFloat() ?: 0f,
        maxTemp = daily.temp?.max?.toFloat() ?: 0f,
        iconId = getIconRes(daily.weather?.getOrNull(0)?.icon),
        time = daily.dt ?: 0,
        timeDebug = currentDateAndTime(Date((daily.dt ?: 0) * 1000))
    )
}

fun mapToCurrentEntity(
    current: Current?,
    hours: List<Hourly?>?,
    city: String?
): CurrentWeatherEntity? {
    current ?: return null
    val beginOfDay = beginOfDay() - 1
    val endOfDay = endOfDay() - 1
    var maxTemp = 0f
    var minTemp = 0f
    hours?.filter { it?.dt in beginOfDay..endOfDay } //костыль для бека
        ?.mapNotNull { it?.temp?.toFloat() }
        ?.let { tempList ->
            maxTemp = tempList.maxBy { it } ?: 0f
            minTemp = tempList.minBy { it } ?: 0f
        }
    return CurrentWeatherEntity(
        city = city ?: "-",
        temp = current.temp?.toFloat() ?: 0f,
        minTemp = minTemp,
        maxTemp = maxTemp,
        feelsLike = current.feelsLike?.toFloat() ?: 0f,
        humidity = current.humidity,
        windSpeed = current.windSpeed,
        pressure = current.pressure,
        dewPoint = current.dewPoint,
        condition = current.weather?.getOrNull(0)?.main,
        iconId = getIconRes(current.weather?.getOrNull(0)?.icon),
        sunrise = current.sunrise ?: 0,
        sunset = current.sunset ?: 0,
        visibility = current.visibility
    )
}

fun mapToDayUI(day: DayEntity): DayUI {
    return DayUI(
        maxTemp = day.maxTemp.roundToInt().toString(),
        minTemp = day.minTemp.roundToInt().toString(),
        dayOfWeek = day.dayOfWeek,
        iconId = day.iconId
    )
}

fun mapToHourUI(hour: HourEntity): HourUI {
    return HourUI(
        time = getHour(Date(hour.time * 1000)).toFloat(),
        temp = hour.temp,
        iconId = hour.iconId
    )
}

fun mapToCurrentUI(current: CurrentWeatherEntity): CurrentUI {
    return CurrentUI(
        city = current.city,
        temp = current.temp.roundToInt().toString(),
        maxTemp = current.maxTemp.roundToInt().toString() + "°" + "C",
        minTemp = current.minTemp.roundToInt().toString() + "°" + "C",
        sunrise = current.sunrise.toFloat(),
        sunset = current.sunset.toFloat(),
        time = (System.currentTimeMillis() / 1000).toFloat(),
        feelsLike = current.feelsLike?.roundToInt().toString() + "°",
        humidity = current.humidity?.toString() + "%" ?: "",
        windSpeed = current.windSpeed?.toString() ?: "",
        pressure = current.pressure?.toString() ?: "",
        visibility = current.visibility?.toString() ?: "",
        condition = current.condition,
        iconId = current.iconId,
        dewPoint = current.dewPoint?.toString() ?: ""
    )
}