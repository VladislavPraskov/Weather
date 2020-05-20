package com.example.weather.data.repository.main

import androidx.lifecycle.LiveData
import com.example.weather.presenter.main.MainResultAction

interface MainRepository {
    fun getWeatherCity(): LiveData<MainResultAction>
}