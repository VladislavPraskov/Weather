package com.devpraskov.android_ext

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue


fun endOfDay(date: Date = Date()): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date.time
    calendar[Calendar.HOUR_OF_DAY] = 23
    calendar[Calendar.MINUTE] = 59
    calendar[Calendar.SECOND] = 59
    calendar[Calendar.MILLISECOND] = 999
    return calendar.time.time / 1000
}

fun beginOfDay(date: Date = Date()): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date.time
    calendar[Calendar.HOUR] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0
    return calendar.time.time / 1000
}


fun beginOfHour(date: Date = Date()): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date.time
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0
    return calendar.time.time / 1000
}

//Fr, 8:24
@SuppressLint("SimpleDateFormat")
fun dayAndTime(date: Date = Date(), offsetSec: Int? = null): String {
    val timeZone = offsetSec?.let {
        TimeZone.getDefault().also { it.rawOffset = offsetSec * 1000 }
    }
    return SimpleDateFormat("EEE, HH:mm", Locale.ENGLISH)
        .apply { if (timeZone != null) setTimeZone(timeZone) }
        .format(date).capitalize()
}

//8:24
@SuppressLint("SimpleDateFormat")
fun time(date: Date = Date(), offsetSec: Int? = null): String {
    val timeZone = offsetSec?.let {
        TimeZone.getDefault().also { it.rawOffset = offsetSec * 1000 }
    }
    return SimpleDateFormat("HH:mm")
        .apply { if (timeZone != null) setTimeZone(timeZone) }
        .format(date).capitalize()
}

//Fr
@SuppressLint("SimpleDateFormat")
fun dayOfWeek(date: Date = Date(), offsetSec: Int?): String {
    val timeZone = offsetSec?.let {
        TimeZone.getDefault().also { it.rawOffset = offsetSec * 1000 }
    }
    return SimpleDateFormat("EEE", Locale.ENGLISH)
        .apply { if (timeZone != null) setTimeZone(timeZone) }
        .format(date).capitalize()
}

// 2020-05-28, 21:22
fun currentDateAndTime(date: Date = Date()): String {
    return SimpleDateFormat("yyyy-MM-dd, HH:mm", Locale.getDefault()).format(date).capitalize()
}

//Либо заданный, либо локальный offset
fun getHour(date: Date = Date(), offsetSec: Int? = null): Int {
    val timeZone = offsetSec?.let { TimeZone.getDefault().also { it.rawOffset = offsetSec * 1000 } }
    val calendar = Calendar.getInstance().apply { if (timeZone != null) setTimeZone(timeZone) }
    calendar.timeInMillis = date.time
    return calendar[Calendar.HOUR_OF_DAY]
}

//3
fun getUtcOffsetHour(date: Date = Date()): Int {
    return TimeZone.getDefault().getOffset(date.time) / 3600000
}

//3
fun getUtcOffsetSec(date: Date = Date()): Long {
    return TimeZone.getDefault().getOffset(date.time) / 1000L
}

//UTC -3:00
fun getUtcOffsetStr(date: Date = Date()): String {
    val utcOffset = TimeZone.getDefault().getOffset(date.time) / 3600000
    val d = if (utcOffset < 0) "-" else "+"
    return "UTC $d${utcOffset.absoluteValue}:00"
}