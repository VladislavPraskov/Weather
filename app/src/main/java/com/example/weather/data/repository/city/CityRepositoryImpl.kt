package com.example.weather.data.repository.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.weather.data.db.WeatherDataBase
import com.example.weather.data.network.CityApiService
import com.example.weather.data.network.NetworkResourse
import com.example.weather.models.CityUI
import com.example.weather.presenter.city.CityResultAction
import com.example.weather.utils.mapToCityUI
import com.example.weather.utils.mapToCityEntity
import com.example.weather.utils.network.safeCacheCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


class CityRepositoryImpl(val db: WeatherDataBase, private val api: CityApiService) :
    CityRepository {

    var loadByQueryJob = Job()
    override suspend fun loadByQuery(q: String): LiveData<CityResultAction> {
        loadByQueryJob.cancel()
        loadByQueryJob = Job()
        return NetworkResourse.create(
            apiCall = { api.getCityByQuery(q) },
            onSuccess = { success -> //здесь мёржим лист с апи и из кэша, из кэша берём то что встречается в api response
                val cache = safeCacheCall(cacheCall = { db.cityDao.loadCities() })
                val cityCache = cache?.map { mapToCityUI(it) }?.toMutableList()
                val apiCity = mapToCityUI(success.value)
                val concatList = apiCity
                    ?.mapNotNull { aC -> cityCache?.find { cC -> aC.idString == cC.idString } }
                    ?.toMutableList()
                apiCity?.let { concatList?.addAll(apiCity) }
                val result = concatList?.distinctBy { it.idString }
                CityResultAction.getSuccessOrEmpty(result, false)
            },
            onError = { CityResultAction.Error(it) }
        ).result.asLiveData(loadByQueryJob + Dispatchers.IO)
    }

    override suspend fun saveCity(city: CityUI): CityResultAction {
        safeCacheCall(cacheCall = { db.cityDao.saveCurrentCity(mapToCityEntity(city)) })
        val listCities = safeCacheCall({ db.cityDao.loadCities() })
        return CityResultAction.CitySaved(listCities?.map { mapToCityUI(it) })
    }

    override suspend fun deleteCity(idString: String) {
        safeCacheCall(cacheCall = { db.cityDao.deleteCity(idString) })
    }

    override suspend fun loadCityFromCache(): CityResultAction {
        val cities = safeCacheCall(cacheCall = { db.cityDao.loadCities() })
        return CityResultAction.getSuccessOrEmpty(cities?.map { mapToCityUI(it) }, true)
    }

}