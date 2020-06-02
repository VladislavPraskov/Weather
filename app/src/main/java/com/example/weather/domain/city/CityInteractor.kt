package com.example.weather.domain.city

import androidx.lifecycle.LiveData
import com.example.weather.models.CityUI
import com.example.weather.presenter.city.CityResultAction

interface CityInteractor {

    fun loadByQuery(q: String): LiveData<CityResultAction>
    suspend fun saveCity(city: CityUI): CityResultAction
    suspend fun loadCityFromCache(): CityResultAction
    suspend fun deleteCity(countryAndPostCode: String)
}