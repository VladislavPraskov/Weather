package com.example.weather.presenter.main.mvi

import com.example.weather.models.main.WeatherUiModel
import com.example.weather.utils.error.ErrorMVI
import com.example.weather.utils.models.Event

data class MainViewState(
    val isLoading: Boolean = false,
    val error: ErrorMVI? = null,
    val time: String? = null,
    val data: Event<WeatherUiModel?> = Event()
)
