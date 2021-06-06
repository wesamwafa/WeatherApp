package com.MyWeather.data.repository

import androidx.lifecycle.LiveData
import com.MyWeather.data.DB.Entity.Alarms
import com.MyWeather.data.DB.Entity.Current
import com.MyWeather.data.NetworkData.Response.CurrentWeatherResponse
import com.MyWeather.data.NetworkData.Response.FavWeatherResponse

interface ForecastRepository {

    suspend fun getCurrentWeather(): LiveData<out CurrentWeatherResponse>
    suspend fun getFavWeather(): LiveData<out FavWeatherResponse>
    suspend fun getAllFavData(): LiveData<MutableList<FavWeatherResponse>>
    suspend fun deleteAllData()
     fun deleteFavItem(favWeatherDelete: FavWeatherResponse)
    fun getAlarms():LiveData<MutableList<Alarms>>
    fun deleteAlarm(alarms: Alarms)

    fun addAlarm(alarms: Alarms)
}