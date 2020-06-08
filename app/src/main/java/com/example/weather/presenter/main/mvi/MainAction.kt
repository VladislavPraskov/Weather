package com.example.weather.presenter.main.mvi

sealed class MainAction {
    object LoadWeather : MainAction()
    object UpdateTime : MainAction()
}
