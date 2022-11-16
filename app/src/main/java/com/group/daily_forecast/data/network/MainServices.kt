package com.group.daily_forecast.data.network

import com.group.daily_forecast.pojo.response.DailyForecastResponse
import com.group.daily_forecast.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MainServices {

    @GET("forecast")
    suspend fun forecast(
        @Query("q") city_name: String,
        @Query("APPID") app_id: String = Constants.DAILY_FORECAST_APP_ID,
    ): Response<DailyForecastResponse>

}