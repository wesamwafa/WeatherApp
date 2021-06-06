package com.MyWeather.data.DB.Entity


import com.google.gson.annotations.SerializedName

data class Daily(
        val clouds: Int,
        val dt: Int,
        @SerializedName("feels_like")
        val feelsLike: FeelsLike,
        val humidity: Int,
        val pressure: Int,
        val rain: Double,
        val snow: Double,
        val sunrise: Int,
        val sunset: Int,
        var temp: Temp,
        val weather: List<Weather>,
        @SerializedName("wind_speed")
        val windSpeed: Double
)