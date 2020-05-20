package com.example.weather.presenter.main

sealed class MainAction {
    data class SomeAction(val value_1: String? = "") : MainAction()
    object SomeAction2 : MainAction()
}
