package com.example.weather.domain.main

import androidx.lifecycle.LiveData
import com.example.weather.data.repository.main.MainRepository
import com.example.weather.presenter.main.MainResultAction

class MainInteractorImpl(private val repository: MainRepository) : MainInteractor {

    override fun getWeatherCity(): LiveData<MainResultAction> {
        return repository.getWeatherCity()
    }

}