package com.group.daily_forecast.utils

import com.group.daily_forecast.utils.Utils.convertFromKelvinToCelsius
import com.group.daily_forecast.utils.Utils.format2Digits
import org.junit.Assert
import org.junit.Test


class UtilsTest {

    @Test
    fun `convertFromKelvinToCelsius with not null value then return success result`() {

        val result = 303.15.convertFromKelvinToCelsius()

        val expected = 30.0

        assert(expected == result)

    }

    @Test
    fun `convertFromKelvinToCelsius with null value then return null`() {

        val result = null?.convertFromKelvinToCelsius()

        Assert.assertNull(result)

    }

    @Test
    fun `convertFromKelvinToCelsius with not null value then return failure result`() {

        val result = 303.15.convertFromKelvinToCelsius()

        val expected = 30.10

        assert(expected != result)

    }


    @Test
    fun `format2Digits with not null value then return success result`() {

        val result = 303.1115.format2Digits()

        val expected = 303.11.toString()

        assert(expected == result)

    }

    @Test
    fun `format2Digits with null value then return null`() {

        val result = null?.format2Digits()

        Assert.assertNull("$result",result)

    }

    @Test
    fun `format2Digits with not null value then return failure result`() {

        val result = 303.15000.format2Digits()

        val expected = 30.10.toString()

        assert(expected != result)

    }


}