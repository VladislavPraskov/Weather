package com.example.weather.utils

import com.devpraskov.android_ext.*
import com.example.weather.data.db.current_weather.CurrentWeatherEntity
import com.example.weather.data.db.day.DayEntity
import com.example.weather.data.db.hour.HourEntity
import com.example.weather.models.WeatherState
import com.example.weather.models.main.CurrentUI
import com.example.weather.models.main.DayUI
import com.example.weather.models.main.HourUI
import com.example.weather.models.main.WeatherResponse.*
import java.util.*
import kotlin.math.min
import kotlin.math.max
import kotlin.math.roundToInt

fun mapToHourEntity(
    hourWeather: Hourly?,
    city: String?,
    timezoneOffset: Long?
): HourEntity? {
    hourWeather ?: return null

    hourWeather.apply {
        val weatherState = WeatherState.create(
            weatherId = weather?.getOrNull(0)?.id ?: 0,
            iconId = weather?.getOrNull(0)?.icon ?: "d"
        )
        return HourEntity(
            city = city ?: "-",
            temp = temp?.toFloat() ?: 0f,
            iconId = weatherState.iconId,
            time = dt ?: 0,
            timeOffset = timezoneOffset?.toInt() ?: 0,
            timeDebug = currentDateAndTime(Date((dt ?: 0) * 1000))
        )
    }
}

fun mapToDayEntity(
    daily: Daily?,
    city: String?,
    timezoneOffset: Long?
): DayEntity? {
    daily ?: return null

    daily.apply {
        val weatherState = WeatherState.create(
            weatherId = weather?.getOrNull(0)?.id ?: 0,
            iconId = weather?.getOrNull(0)?.icon ?: "d"
        )
        return DayEntity(
            city = city ?: "-",
            dayOfWeek = dayOfWeek(Date((dt ?: 0) * 1000), timezoneOffset?.toInt() ?: 0),
            minTemp = temp?.min?.toFloat() ?: 0f,
            maxTemp = temp?.max?.toFloat() ?: 0f,
            iconId = weatherState.iconId,
            time = dt ?: 0,
            timeOffset = timezoneOffset?.toInt() ?: 0,
            timeDebug = currentDateAndTime(Date((dt ?: 0) * 1000))
        )
    }
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
        val weatherState = WeatherState.create(
            weatherId = weather?.getOrNull(0)?.id ?: 0,
            sunrise = sunrise ?: 0,
            sunset = sunset ?: 0,
            iconId = weather?.getOrNull(0)?.icon ?: "d"
        )
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
            time = dt,
            condition = weather?.getOrNull(0)?.main,
            iconId = weatherState.iconId,
            sunrise = sunrise ?: 0,
            sunset = sunset ?: 0,
            colorStartId = weatherState.colorStartId,
            colorEndId = weatherState.colorEndId,
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
        timeH = getHour(Date(hour.time * 1000), hour.timeOffset).toFloat(),
        timeS = hour.time,
        temp = hour.temp,
        iconId = hour.iconId
    )
}

fun mapToCurrentUI(
    current: CurrentWeatherEntity?,
    hourUi: HourUI?
): CurrentUI? {

    current ?: return null
    fun getHourAndMinute(minutes: Long): String {
        val hour = minutes / 60
        val minute = minutes % 60
        return "$hour ч $minute м"
    }

    fun getVisibility(visibility: Long): String {
        return if (visibility > 1000) "%.0f".format(visibility / 1000.0) + " km"
        else visibility.toString()
    }

    val icon = if (hourUi?.timeS ?: 0L > current.time ?: 0L) hourUi?.iconId else current.iconId
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
            iconId = icon ?: iconId,
            dewPoint = dewPoint?.roundToInt()?.toString() + "°",
            colorStartId = colorStartId,
            colorEndId = colorEndId
        )
    }
}