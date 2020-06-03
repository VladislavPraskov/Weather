package com.example.weather.data.repository.city

import androidx.lifecycle.LiveData
import com.example.weather.data.db.WeatherDataBase
import com.example.weather.data.network.CityApiService
import com.example.weather.data.network.NetworkResourse
import com.example.weather.models.CityUI
import com.example.weather.presenter.city.CityResultAction
import com.example.weather.utils.mapToCityUI
import com.example.weather.utils.mapToCityEntity
import com.example.weather.utils.network.safeCacheCall


class CityRepositoryImpl(val db: WeatherDataBase, private val api: CityApiService) :
    CityRepository {

    override fun loadByQuery(q: String): LiveData<CityResultAction> {
        return NetworkResourse.create(
            apiCall = { api.getCityByQuery(q) },
            onSuccess = { success ->
                CityResultAction.getSuccessOrEmpty(mapToCityUI(success.value), false)
            },
            onError = { CityResultAction.Error(it) }
        ).result
    }

    override suspend fun saveCity(city: CityUI): CityResultAction {
        safeCacheCall(cacheCall = { db.cityDao.saveCurrentCity(mapToCityEntity(city)) })
        val listCities = safeCacheCall({ db.cityDao.loadCities() })
        return CityResultAction.CitySaved(listCities?.map { mapToCityUI(it) })
    }

    override suspend fun deleteCity(countryAndPostCode: String) {
        safeCacheCall(cacheCall = { db.cityDao.deleteCity(countryAndPostCode) })
    }

    override suspend fun loadCityFromCache(): CityResultAction {
        val cities = safeCacheCall(cacheCall = { db.cityDao.loadCities() })
        return CityResultAction.getSuccessOrEmpty(cities?.map { mapToCityUI(it) }, true)
    }

}