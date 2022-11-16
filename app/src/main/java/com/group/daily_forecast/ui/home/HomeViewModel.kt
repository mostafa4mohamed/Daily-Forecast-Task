package com.group.daily_forecast.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.group.daily_forecast.data.local.MainDAO
import com.group.daily_forecast.data.network.MainServices
import com.group.daily_forecast.pojo.response.*
import com.group.daily_forecast.utils.Constants
import com.group.daily_forecast.utils.NetworkState
import com.group.daily_forecast.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: HomeRepository
) :
    ViewModel() {

    private val _forecastStateFlow = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val forecastStateFlow: MutableStateFlow<NetworkState> get() = _forecastStateFlow

    fun forecast(cityName: String) {

        _forecastStateFlow.value = NetworkState.Loading

        viewModelScope.launch(dispatcher) {

            kotlin.runCatching {
                repository.forecast(cityName)
            }.onFailure {

                when (it) {
                    is UnknownHostException ->
                        _forecastStateFlow.value =
                            NetworkState.Error(Constants.Codes.EXCEPTIONS_CODE)
                    is java.net.ConnectException ->
                        _forecastStateFlow.value =
                            NetworkState.Error(Constants.Codes.EXCEPTIONS_CODE)
                    else -> _forecastStateFlow.value =
                        NetworkState.Error(Constants.Codes.UNKNOWN_CODE)
                }

            }.onSuccess { result ->

                if (result is Response<*>) {

                    if (result.body() != null) {
                        _forecastStateFlow.value = NetworkState.Result(result.body())
                        repository.cashingForecast(result.body() as DailyForecastResponse)
                    } else {

                        val message=getMessage(result)

                        _forecastStateFlow.value = NetworkState.Error(Constants.Codes.UNKNOWN_CODE,message)
                    }
                } else {

                    val data = result as List<DailyForecastData>

                    if (data.isEmpty()) {
                        _forecastStateFlow.value = NetworkState.Error(Constants.Codes.UNKNOWN_CODE)
                        return@launch
                    }

                    data.forEach {
                        Log.e(TAG, "forecast: ${it.weather}")
                    }

                    val response =
                        DailyForecastResponse(cod = Constants.Codes.OFFLINE_CODE.toString())
                    response.list = result

                    _forecastStateFlow.value = NetworkState.Result(response)
                }

            }

        }
    }

    private fun getMessage(response: Response<*>): String? {

        val gson = Gson()
        val type = object : TypeToken<ErrorResponse>() {}.type
        val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)

        Log.e(TAG, "getMessage: $errorResponse")

        return errorResponse?.message
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}


class HomeRepository @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val mainServices: MainServices,
    private val mainDAO: MainDAO
) {

    suspend fun forecast(cityName: String): Any {
        return if (Utils.isInternetAvailable()) {
            remoteForecast(cityName)
        } else {
            offLineForecast(cityName)
        }
    }

    private suspend fun remoteForecast(cityName: String) = mainServices.forecast(cityName)

    private fun offLineForecast(cityName: String): List<DailyForecastData> {

        val cities = mainDAO.checkCity(cityName)

        Log.e(TAG, "offLineForecast: $cities")

        return if (cities.isEmpty())
            emptyList()
        else
            mainDAO.getForecasts(cities.first().id)
    }

    fun cashingForecast(body: DailyForecastResponse) {

        CoroutineScope(dispatcher).launch {

            clearCashByCityId(body.city!!.id)

            if (!body.list.isNullOrEmpty()) {

                addCity(body.city)

                body.list!!.map {
                    it.city_id = body.city!!.id
                    mainDAO.insertForecast(it)
                }
            }
        }

    }

    private fun clearCashByCityId(cityId: Int) {
        mainDAO.clearDailyForecastByCityId(cityId)
    }

    private fun addCity(city: City?) {

        if (city != null)
            mainDAO.insertCity(city)

    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}