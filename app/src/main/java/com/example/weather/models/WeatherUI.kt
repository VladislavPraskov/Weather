package com.example.weather.models

import android.os.Parcelable
import com.example.weather.models.main.CurrentUI
import com.example.weather.models.main.DayUI
import com.example.weather.models.main.HourUI
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherUI(
    val current: CurrentUI,
    val days: List<DayUI>,
    val hours: List<HourUI>
) : Parcelable
