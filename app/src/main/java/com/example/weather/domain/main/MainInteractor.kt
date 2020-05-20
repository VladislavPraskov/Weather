package com.example.weather.domain.main

import androidx.lifecycle.LiveData
import com.example.weather.presenter.main.MainResultAction

interface MainInteractor {
    fun getWeatherCity(): LiveData<MainResultAction>
}