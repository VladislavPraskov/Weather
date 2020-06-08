package com.example.weather.data.repository.main

import androidx.lifecycle.LiveData
import com.example.weather.presenter.main.mvi.MainResultAction

interface MainRepository {
    suspend fun getWeather(): LiveData<MainResultAction>
}