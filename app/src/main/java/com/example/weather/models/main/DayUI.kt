package com.example.weather.models.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DayUI(
    val time: Long,
    var maxTemp: Float = 0f,
    var minTemp: Float = 0f,
    val dayOfWeek: String,
    val iconId: Int
): Parcelable