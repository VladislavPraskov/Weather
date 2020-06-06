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
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max
import kotlin.math.roundToInt

fun mapToHourEntity(
    hourWeather: Hourly?,
    city: String?,
    timezoneOffset: Long?
): HourEntity? {
    hourWeather ?: return null
    return HourEntity(
        city = city ?: "-",
        temp = hourWeather.temp?.toFloat() ?: 0f,
        iconId = getIconRes(hourWeather.weather?.getOrNull(0)?.icon),
        time = hourWeather.dt ?: 0,
        timeOffset = timezoneOffset?.toInt() ?: 0,
        timeDebug = currentDateAndTime(Date((hourWeather.dt ?: 0) * 1000))
    )
}

fun mapToDayEntity(
    daily: Daily?,
    city: String?,
    timezoneOffset: Long?
): DayEntity? {
    daily ?: return null
    return DayEntity(
        city = city ?: "-",
        dayOfWeek = dayOfWeek(Date((daily.dt ?: 0) * 1000), timezoneOffset?.toInt() ?: 0),
        minTemp = daily.temp?.min?.toFloat() ?: 0f,
        maxTemp = daily.temp?.max?.toFloat() ?: 0f,
        iconId = getIconRes(daily.weather?.getOrNull(0)?.icon),
        time = daily.dt ?: 0,
        timeOffset = timezoneOffset?.toInt() ?: 0,
        timeDebug = currentDateAndTime(Date((daily.dt ?: 0) * 1000))
    )
}

fun mapToCurrentEntity(
    current: Current?,
    hours: List<Hourly?>?,
    day: Daily?,
    timezoneOffset: Long?,
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
    current.apply {
        return CurrentWeatherEntity(
            city = city ?: "-",
            temp = current.temp?.toFloat() ?: 0f,
            timeOffset = timezoneOffset?.toInt() ?: 0,
            minTemp = min(minTemp, day?.temp?.min?.toFloat() ?: 100f),
            maxTemp = max(maxTemp, day?.temp?.max?.toFloat() ?: -100f),
            feelsLike = feelsLike?.toFloat() ?: 0f,
            humidity = humidity,
            windSpeed = windSpeed,
            pressure = pressure,
            dewPoint = dewPoint,
            condition = weather?.getOrNull(0)?.main,
            iconId = getIconRes(weather?.getOrNull(0)?.icon),
            sunrise = sunrise ?: 0,
            sunset = sunset ?: 0,
            visibility = visibility
        )
    }
}

fun mapToDayUI(day: DayEntity): DayUI {
    return DayUI(
        maxTemp = day.maxTemp.roundToInt().toString() + "°",
        minTemp = day.minTemp.roundToInt().toString() + "°",
        dayOfWeek = day.dayOfWeek,
        iconId = day.iconId
    )
}

fun mapToHourUI(hour: HourEntity): HourUI {
    return HourUI(
        time = getHour(Date(hour.time * 1000), hour.timeOffset).toFloat(),
        temp = hour.temp,
        iconId = hour.iconId
    )
}

fun mapToCurrentUI(
    current: CurrentWeatherEntity,
    hourUi: HourUI?
): CurrentUI {
    fun getHourAndMinute(minutes: Long): String {
        val hour = minutes / 60
        val minute = minutes % 60
        return "$hour ч $minute м"
    }

    fun getVisibility(visibility: Long): String {
        return if (visibility > 1000) "%.0f".format(visibility / 1000.0) + " km"
        else visibility.toString()
    }

    current.apply {
        return CurrentUI(
            city = city,
            temp = (hourUi?.temp ?: temp).roundToInt().toString(),
            maxTemp = maxTemp.roundToInt().toString() + "°" + "C",
            minTemp = minTemp.roundToInt().toString() + "°" + "C",
            sunrise = sunrise.toFloat(),
            sunset = sunset.toFloat(),
            sunDayH = getHourAndMinute((sunset - sunrise) / 60),
            sunsetH = time(Date(sunset * 1000), timeOffset),
            sunriseH = time(Date(sunrise * 1000), timeOffset),
            time = currentTimeSec.toFloat(),
            timeOffset = timeOffset,
            feelsLike = feelsLike?.roundToInt().toString() + "°",
            humidity = humidity?.toString() + "%",
            windSpeed = windSpeed?.toString() + " m/s",
            pressure = "${((pressure ?: 0) * 0.75).roundToInt()} mm",
            visibility = getVisibility(visibility ?: 0),
            condition = condition,
            iconId = hourUi?.iconId ?: iconId,
            dewPoint = dewPoint?.roundToInt()?.toString() + "°"
        )
    }


}