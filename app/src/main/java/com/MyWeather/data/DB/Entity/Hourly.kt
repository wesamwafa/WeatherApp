package com.MyWeather.data.DB.Entity


import com.google.gson.annotations.SerializedName

data class Hourly(
        val clouds: Int,
        val dt: Int,
        @SerializedName("feels_like")
        val feelsLike: Double,
        val humidity: Int,
        val pressure: Int,
        val temp: Double,
        val visibility: Double,
        val weather: List<Weather>
)