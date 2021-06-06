package com.MyWeather.data.NetworkData

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.MyWeather.data.NetworkData.Response.CurrentWeatherResponse
import com.MyWeather.data.NetworkData.Response.FavWeatherResponse
import retrofit2.await
import java.io.IOException

class WeatherNetworkDataSourceImpl(private val apiService: ApiService ) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(latitude: String, longitude: String, language: String, key: String, exclude: String) {
        try {

            val fetchedCurrentWeather = apiService.getCurrentWeather(longitude, latitude, language, key, exclude)
                    .await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }
        catch (e:IOException){

            Log.e("TAAGAAA","No INTERNET",e)
        }
    }
    private val _downloadedFavWeather = MutableLiveData<FavWeatherResponse>()
    override val downloadedFavWeather: LiveData<FavWeatherResponse>
        get() = _downloadedFavWeather

    override suspend fun fetchFavWeather(latitude: String, longitude: String, language: String, key: String, exclude: String) {
        try {

            val fetchedFavWeather = apiService.getFavWeather(longitude, latitude, language, key, exclude)
                    .await()
            _downloadedFavWeather.postValue(fetchedFavWeather)
        }
        catch (e:IOException){

            Log.e("TAAGAAA","No INTERNET",e)
        }
    }
}