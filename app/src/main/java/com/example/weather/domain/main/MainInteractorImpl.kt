package com.example.weather.domain.main

import androidx.lifecycle.LiveData
import com.example.weather.data.repository.main.MainRepository
import com.example.weather.presenter.main.mvi.MainResultAction

class MainInteractorImpl(private val repository: MainRepository) : MainInteractor {
    override fun getCurrentCity(): LiveData<MainResultAction> {
        return repository.getCurrentCity()
    }

    override fun getCityByName(city: String): LiveData<MainResultAction> {
        return repository.getCityByName(city)
    }

}