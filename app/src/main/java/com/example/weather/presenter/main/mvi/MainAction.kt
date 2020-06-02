package com.example.weather.presenter.main.mvi

sealed class MainAction {
    object LoadCurrentCity : MainAction()
    object UpdateTime : MainAction()
}
