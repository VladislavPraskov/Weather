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

    @Query("DELETE FROM city_entity WHERE idString = :idString")
    fun deleteCity(idString: String)

    @Query("SELECT * FROM city_entity ORDER BY cityName ASC")
    fun loadCities(): List<CityEntity>

    @Query("UPDATE city_entity SET isCurrentSelected = 1 WHERE idString = :idString")
    fun updateChecked(idString: String): Long

    @Query("UPDATE city_entity SET isCurrentSelected = 0 WHERE isCurrentSelected = 1")
    fun deleteCurrentCityFlag()

    @Transaction
    fun saveCurrentCity(cityEntity: CityEntity) {
        deleteCurrentCityFlag()
        saveCity(cityEntity.copy(isCurrentSelected = true))
    }

    @Transaction
    fun setChecked(cityEntity: CityEntity) {
        val q = updateChecked(cityEntity.idString)
        deleteCurrentCityFlag()
        if (q == 0L) saveCity(cityEntity.copy(isCurrentSelected = true))
    }
}
