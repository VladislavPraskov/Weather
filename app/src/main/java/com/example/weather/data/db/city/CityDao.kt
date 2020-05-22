package com.example.weather.data.db.city

import androidx.room.*
import com.example.weather.data.db.city.City

@Dao
interface CityDao {

    @Query("SELECT * FROM city WHERE isCurrentSelected = 1 LIMIT 1")
    fun getCurrentCity(): City?

    @Query("SELECT name FROM city WHERE isCurrentSelected = 1 LIMIT 1")
    fun getCurrentCityName(): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCity(city: City): Long

    @Query("UPDATE City SET isCurrentSelected = 0 WHERE isCurrentSelected = 1")
    fun deleteCurrentCityFlag()

    @Transaction
    fun saveCurrentCity(city: City){
        deleteCurrentCityFlag()
        saveCity(city.copy(isCurrentSelected = true))
    }
}
