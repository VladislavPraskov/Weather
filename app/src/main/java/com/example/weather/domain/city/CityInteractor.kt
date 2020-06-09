package com.example.weather.domain.city

import androidx.lifecycle.LiveData
import com.example.weather.models.city.CityUI
import com.example.weather.presenter.city.mvi.CityResultAction

interface CityInteractor {

    suspend fun loadByQuery(q: String): LiveData<CityResultAction>
    suspend fun saveCity(city: CityUI): CityResultAction
    suspend fun loadCityFromCache(): CityResultAction
    suspend fun deleteCity(idString: String)
}