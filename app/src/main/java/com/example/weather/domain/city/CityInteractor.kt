package com.example.weather.domain.city

import androidx.lifecycle.LiveData
import com.example.weather.presenter.city.CityResultAction

interface CityInteractor {

    fun loadByQuery(q: String): LiveData<CityResultAction>
}