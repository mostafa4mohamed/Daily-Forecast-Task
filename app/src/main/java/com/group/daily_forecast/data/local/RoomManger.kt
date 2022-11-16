package com.group.daily_forecast.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.group.daily_forecast.pojo.response.City
import com.group.daily_forecast.pojo.response.DailyForecastData

@Database(
    entities = [City::class, DailyForecastData::class],
    version = 3,
    exportSchema = true,
)
@TypeConverters(ConverterHelper::class)
abstract class RoomManger : RoomDatabase() {

    abstract fun mainDAO(): MainDAO

}