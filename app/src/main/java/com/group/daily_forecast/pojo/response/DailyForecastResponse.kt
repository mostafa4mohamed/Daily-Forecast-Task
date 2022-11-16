package com.group.daily_forecast.pojo.response

import androidx.room.*

data class DailyForecastResponse(
    var city: City? = null,
    var cnt: Int? = null,
    var cod: String? = null,
    var list: List<DailyForecastData>? = null,
    var message: Int? = null
)

@Entity(ignoredColumns = ["coord", "country", "timezone", "sunset", "sunrise", "population"])
data class City(
    @PrimaryKey var id: Int,
    @ColumnInfo var name: String,
    /*@Ignore*/ var coord: Coord?,
    /*@Ignore*/ var country: String,
    /*@Ignore*/ var population: Int,
    /*@Ignore*/ var sunrise: Int,
    /*@Ignore*/ var sunset: Int,
    /*@Ignore*/ var timezone: Int
) {
    constructor() : this(0, "", null, "", 0, 0, 0, 0)
}

@Entity
data class DailyForecastData(
    @PrimaryKey(autoGenerate = true) var daily_forecast_id: Int? = null,
    @ColumnInfo var city_id: Int? = null,
    @ColumnInfo val clouds: Clouds? = null,
    @ColumnInfo val dt: Int? = null,
    @ColumnInfo val dt_txt: String? = null,
    @ColumnInfo val main: Main? = null,
    @ColumnInfo val pop: Int? = null,
    @ColumnInfo val sys: Sys? = null,
    @ColumnInfo val visibility: Int? = null,
    @ColumnInfo val weather: List<Weather>? = null,
    @ColumnInfo val wind: Wind? = null
)

data class Coord(
    val lat: Double,
    val lon: Double
)

data class Clouds(
    val all: Int
)

data class Main(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_kf: Double,
    val temp_max: Double,
    val temp_min: Double
)

data class Sys(
    val pod: String
)

data class Weather(
    val description: String,
    val icon: String,
    val id: Double,
    val main: String
)

data class Wind(
    val deg: Int,
    val gust: Double,
    val speed: Double
)