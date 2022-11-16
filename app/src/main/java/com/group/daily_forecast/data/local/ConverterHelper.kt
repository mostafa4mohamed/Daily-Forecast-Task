package com.group.daily_forecast.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.group.daily_forecast.pojo.response.*
import java.lang.reflect.Type


class ConverterHelper {

    @TypeConverter
    fun fromClouds(clouds: Clouds?): String? {
        if (clouds == null) {
            return null
        }
        val gson = Gson()
        return gson.toJson(clouds, clouds::class.java)
    }

    @TypeConverter
    fun toClouds(josnString: String?): Clouds? {
        if (josnString == null) {
            return null
        }
        val gson = Gson()
        return gson.fromJson(josnString, Clouds::class.java)
    }

    @TypeConverter
    fun fromMain(main: Main?): String? {
        if (main == null) {
            return null
        }
        val gson = Gson()
        return gson.toJson(main, main::class.java)
    }

    @TypeConverter
    fun toMain(josnString: String?): Main? {
        if (josnString == null) {
            return null
        }
        val gson = Gson()
        return gson.fromJson(josnString, Main::class.java)
    }

    @TypeConverter
    fun fromSys(sys: Sys?): String? {
        if (sys == null) {
            return null
        }
        val gson = Gson()
        return gson.toJson(sys, sys::class.java)
    }

    @TypeConverter
    fun toSys(josnString: String?): Sys? {
        if (josnString == null) {
            return null
        }
        val gson = Gson()
        return gson.fromJson(josnString, Sys::class.java)
    }

    @TypeConverter
    fun fromWind(wind: Wind?): String? {
        if (wind == null) {
            return null
        }
        val gson = Gson()
        return gson.toJson(wind, wind::class.java)
    }

    @TypeConverter
    fun toWind(josnString: String?): Wind? {
        if (josnString == null) {
            return null
        }
        val gson = Gson()
        return gson.fromJson(josnString, Wind::class.java)
    }

    @TypeConverter
    fun fromListWeather(list: List<Weather>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        return gson.toJson(list, List::class.java)
    }

    @TypeConverter
    fun toListWeather(josnString: String?): List<Weather>? {
        if (josnString == null) {
            return null
        }
        val gson = Gson()
        val listType: Type =
            object : TypeToken<List<Weather?>?>() {}.type
        return gson.fromJson<List<Weather>>(josnString, listType)
    }

    @TypeConverter
    fun fromCoord(coord: Coord?): String? {
        if (coord == null) {
            return null
        }
        val gson = Gson()
        return gson.toJson(coord, coord::class.java)
    }

    @TypeConverter
    fun toCoord(josnString: String?): Coord? {
        if (josnString == null) {
            return null
        }
        val gson = Gson()
        return gson.fromJson(josnString, Coord::class.java)
    }

}