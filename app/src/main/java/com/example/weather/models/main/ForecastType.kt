package com.example.weather.models.main

import androidx.annotation.IntDef


@IntDef(CURRENT, MINUTE, HOURLY, DAILY)
@Retention(AnnotationRetention.SOURCE)
annotation class ForecastType

const val CURRENT = 0 //current weather
const val MINUTE = 1 //forecast for 1 hour
const val HOURLY = 2 //forecast for 48 hours
const val DAILY = 3 //forecast for 7 days