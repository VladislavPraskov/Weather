package com.example.weather.domain.city

import androidx.lifecycle.LiveData
import com.example.weather.data.repository.city.CityRepository
import com.example.weather.presenter.city.CityResultAction

class CityInteractorImpl(val repository: CityRepository) : CityInteractor {
    fun loadCityFromCache() {

    }

    override fun loadByQuery(q: String): LiveData<CityResultAction> {
        return repository.loadByQuery(q)
    }
}

