package com.example.weather.models.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HourUI(
    val timeH: Float,
    val timeS: Long,
    val temp: Float = 0f,
    var iconId: Int
): Parcelable