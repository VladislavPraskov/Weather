package com.example.weather.presenter.city

import com.example.weather.models.CityUI
import com.example.weather.utils.error.ErrorMVI
import com.example.weather.utils.models.Event

data class CityViewState(
    val isLoading: Boolean = false,
    val isCache: Boolean = false,
    val isNotFound: Boolean = false,
    val error: Event<ErrorMVI?> = Event(),
    val data: Event<List<CityUI>?> = Event()
)
