package com.group.daily_forecast.utils

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import java.net.InetAddress
import java.util.*

object Utils {

    fun showToast(mContext: Context, msg: String) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun isInternetAvailable(): Boolean {
        return try {
            val ipAddr: InetAddress = InetAddress.getByName("google.com")
            //You can replace it with your name
            !ipAddr.equals("")
        } catch (e: Exception) {
            false
        }
    }

    fun Double.convertFromKelvinToCelsius(): Double = this - 273.15

    fun Double?.format2Digits(): String = String.format(Locale.ENGLISH, "%.2f", this)

}