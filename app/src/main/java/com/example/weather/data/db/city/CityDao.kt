package com.example.weather.data.db.city

import androidx.room.*

@Dao
interface CityDao {

    @Query("SELECT * FROM city_entity WHERE isCurrentSelected = 1 LIMIT 1")
    fun getCurrentCity(): CityEntity?

    @Query("SELECT name FROM city_entity WHERE isCurrentSelected = 1 LIMIT 1")
    fun getCurrentCityName(): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCity(cityEntity: CityEntity): Long

    @Query("UPDATE city_entity SET isCurrentSelected = 0 WHERE isCurrentSelected = 1")
    fun deleteCurrentCityFlag()

    @Transaction
    fun saveCurrentCity(cityEntity: CityEntity){
        deleteCurrentCityFlag()
        saveCity(cityEntity.copy(isCurrentSelected = true))
    }
}
