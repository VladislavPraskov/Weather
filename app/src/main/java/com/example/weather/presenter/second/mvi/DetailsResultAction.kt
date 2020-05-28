package com.example.weather.presenter.second.mvi

sealed class DetailsResultAction {
    object CurrentAndDailyForecast : DetailsResultAction()
}
