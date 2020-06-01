package com.example.weather.data.repository.city

import androidx.lifecycle.LiveData
import com.example.weather.presenter.city.CityResultAction

interface CityRepository {
    fun loadByQuery(q: String): LiveData<CityResultAction>
}