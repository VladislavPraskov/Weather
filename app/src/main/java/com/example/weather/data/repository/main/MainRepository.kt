package com.example.weather.data.repository.main

import androidx.lifecycle.LiveData
import com.example.weather.presenter.main.mvi.MainResultAction

interface MainRepository {
    fun getCurrentCity(): LiveData<MainResultAction>
    fun getCityByName(city: String): LiveData<MainResultAction>
}