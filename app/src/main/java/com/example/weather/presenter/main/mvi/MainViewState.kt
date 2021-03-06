package com.example.weather.presenter.main.mvi

import com.example.weather.models.WeatherUI
import com.example.weather.utils.error.ErrorMVI
import com.example.weather.utils.models.Event

data class MainViewState(
    val isLoading: Boolean = false,
    val error: Event<ErrorMVI?> = Event(),
    val time: String? = null,
    val timeOffset: Int = 0,
    val data: Event<WeatherUI?> = Event()
)
