package com.MyWeather.data.NetworkData

import com.MyWeather.data.NetworkData.Response.CurrentWeatherResponse
import com.MyWeather.data.NetworkData.Response.FavWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query



var EXCLUDE = "minutely"
const val API_KEY = "f0b225512c675961671687d24f08f997"
var LANGUAGE = "en"
var LATITUDEFAV = "31.2001"
var LONGITUDE = "31.3785"
var LATITUDE = "31.0409"
var LONGITUDEFAV = "29.9187"


interface ApiService {
    //https://api.openweathermap.org/data/2.5/onecall?lat=33.441792&lon=-94.037689&exclude=hourly,minutely&APPID=f0b225512c675961671687d24f08f997
    @GET("onecall")
    fun getCurrentWeather(
            @Query("lat") latitude: String ,
            @Query("lon") longitude: String ,
            @Query("lang") language: String = LANGUAGE,
            @Query("APPID") key: String = API_KEY,
            @Query("exclude") exclude: String = EXCLUDE,
            @Query("units") units: String = "metric"
    ): Deferred<CurrentWeatherResponse>

    @GET("onecall")
    fun getFavWeather(
            @Query("lat") latitude: String = LATITUDEFAV,
            @Query("lon") longitude: String = LONGITUDEFAV,
            @Query("lang") language: String = LANGUAGE,
            @Query("APPID") key: String = API_KEY,
            @Query("exclude") exclude: String = EXCLUDE,
            @Query("units") units: String = "metric"
    ): Deferred<FavWeatherResponse>

    companion object {
        operator fun invoke(connectionInterceptor: ConnectionInterceptor)
                : ApiService {
            val requestInterceptor = Interceptor {
                val url = it.request()
                        .url()
                        .newBuilder()
                        .build()
                val request = it.request()
                        .newBuilder()
                        .url(url)
                        .build()
                return@Interceptor it.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(requestInterceptor)
                    .addInterceptor(connectionInterceptor)
                    .build()

            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .build()
                    .create(ApiService::class.java)
        }


    }
}