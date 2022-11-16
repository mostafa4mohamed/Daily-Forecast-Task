package com.group.daily_forecast.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.group.daily_forecast.pojo.response.City
import com.group.daily_forecast.pojo.response.DailyForecastData

@Dao
interface MainDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(data: City)

    @Query("SELECT * FROM City WHERE name =:cityName COLLATE NOCASE")
    fun checkCity(cityName: String): List<City>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForecast(data: DailyForecastData)

    @Query("SELECT * FROM DailyForecastData WHERE city_id =:cityId")
    fun getForecasts(cityId: Int): List<DailyForecastData>

    @Query("DELETE FROM DailyForecastData WHERE city_id =:cityId")
    fun clearDailyForecastByCityId(cityId: Int)

}