package com.example.weather.domain.city

import androidx.lifecycle.LiveData
import com.example.weather.data.repository.city.CityRepository
import com.example.weather.models.CityUI
import com.example.weather.presenter.city.CityResultAction

class CityInteractorImpl(private val repository: CityRepository) : CityInteractor {

    override fun loadByQuery(q: String): LiveData<CityResultAction> {
        return repository.loadByQuery(q)
    }

    override suspend fun saveCity(city: CityUI): CityResultAction {
        return repository.saveCity(city)
    }

    override suspend fun deleteCity(countryAndPostCode: String) {
        repository.deleteCity(countryAndPostCode)
    }

    override suspend fun loadCityFromCache(): CityResultAction {
        return repository.loadCityFromCache()
    }
}

