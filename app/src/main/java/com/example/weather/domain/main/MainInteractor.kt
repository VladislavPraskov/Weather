package com.example.weather.domain.main

import androidx.lifecycle.LiveData
import com.example.weather.presenter.main.mvi.MainResultAction

interface MainInteractor {
    suspend fun getWeather(): LiveData<MainResultAction>
}