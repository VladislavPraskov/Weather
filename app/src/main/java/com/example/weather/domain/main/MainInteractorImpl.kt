package com.example.weather.domain.main

import androidx.lifecycle.LiveData
import com.example.weather.data.repository.main.MainRepository
import com.example.weather.presenter.main.mvi.MainResultAction

class MainInteractorImpl(private val repository: MainRepository) : MainInteractor {
    override suspend fun getWeather(): LiveData<MainResultAction> { //todo изменить имя метода
        return repository.getWeather()
    }
}