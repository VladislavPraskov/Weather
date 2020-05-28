package com.example.weather.models.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HourUI(
    val time: Float,
    val temp: Float = 0f,
    val iconId: Int
): Parcelable