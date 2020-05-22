package com.example.weather.domain.main

import androidx.lifecycle.LiveData
import com.example.weather.presenter.main.mvi.MainResultAction

interface MainInteractor {
    fun getCityByName(city: String): LiveData<MainResultAction>
    fun getCurrentCity(): LiveData<MainResultAction>
}