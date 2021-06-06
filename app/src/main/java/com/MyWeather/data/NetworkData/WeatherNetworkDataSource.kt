package com.MyWeather.data.NetworkData

import androidx.lifecycle.LiveData
import com.MyWeather.data.NetworkData.Response.CurrentWeatherResponse
import com.MyWeather.data.NetworkData.Response.FavWeatherResponse


interface WeatherNetworkDataSource {

    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
    val downloadedFavWeather: LiveData<FavWeatherResponse>


    suspend fun fetchCurrentWeather(
            latitude: String,
            longitude: String,
            language: String,
            key: String,
            exclude: String


    )
    suspend fun fetchFavWeather(
            latitude: String,
            longitude: String,
            language: String,
            key: String,
            exclude: String


    )
}