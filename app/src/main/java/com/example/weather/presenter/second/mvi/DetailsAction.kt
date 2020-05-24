package com.example.weather.presenter.second.mvi

sealed class DetailsAction {
    data class SomeAction(val value_1: String? = "") : DetailsAction()
    object SomeAction2 : DetailsAction()
}
