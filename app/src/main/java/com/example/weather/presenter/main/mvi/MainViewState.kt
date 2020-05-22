package com.example.weather.presenter.main.mvi

data class MainViewState(
    val isLoading: Boolean = false,
    val error: String? = null
//   val data : Event<T> = Event()
)
