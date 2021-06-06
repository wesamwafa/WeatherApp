package com.MyWeather.data.DB

import androidx.room.TypeConverter
import com.MyWeather.data.DB.Entity.*
import com.MyWeather.data.NetworkData.Response.CurrentWeatherResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converters {

    @TypeConverter
    fun listToJson (value: List<Weather>?) = Gson().toJson(value)


    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Weather>::class.java).toList()

    @TypeConverter
    fun dailyListToJson (value: List<Daily>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToDailyList(value: String) = Gson().fromJson(value, Array<Daily>::class.java).toList()

    @TypeConverter
    fun jsonToHourlyList(value: String) = Gson().fromJson(value, Array<Hourly>::class.java).toList()

    @TypeConverter
    fun HourlyListToJson (value: List<Hourly>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToAlertsList(value: String) = Gson().fromJson(value, Array<Alerts>::class.java).toList()

    @TypeConverter
    fun alertsListToJson (value: List<Alerts>?) = Gson().toJson(value)








}


