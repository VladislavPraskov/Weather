package com.example.weather.models.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DayUI(
    var maxTemp: String = "",
    var minTemp: String = "",
    val dayOfWeek: String,
    val iconId: Int
): Parcelable