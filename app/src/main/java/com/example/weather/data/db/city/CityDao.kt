package com.example.weather.data.db.city

import androidx.room.*

@Dao
interface CityDao {

    @Query("SELECT * FROM city_entity WHERE isCurrentSelected = 1 LIMIT 1")
    fun getCurrentCity(): CityEntity?

    @Query("SELECT cityName FROM city_entity WHERE isCurrentSelected = 1 LIMIT 1")
    fun getCurrentCityName(): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCity(cityEntity: CityEntity): Long

    @Query("DELETE FROM city_entity WHERE countryAndPostCode = :countryAndPostCode")
    fun deleteCity(countryAndPostCode: String)

    @Query("SELECT * FROM city_entity")
    fun loadCities(): List<CityEntity>

    @Query("UPDATE city_entity SET isCurrentSelected = 1 WHERE countryAndPostCode = :countryAndPostCode")
    fun updateChecked(countryAndPostCode: String): Long

    @Query("UPDATE city_entity SET isCurrentSelected = 0 WHERE isCurrentSelected = 1")
    fun deleteCurrentCityFlag()

    @Transaction
    fun saveCurrentCity(cityEntity: CityEntity) {
        deleteCurrentCityFlag()
        saveCity(cityEntity.copy(isCurrentSelected = true))
    }

    @Transaction
    fun setChecked(cityEntity: CityEntity) {
        val q = updateChecked(cityEntity.countryAndPostCode)
        deleteCurrentCityFlag()
        if (q == 0L) saveCity(cityEntity.copy(isCurrentSelected = true))
    }
}
