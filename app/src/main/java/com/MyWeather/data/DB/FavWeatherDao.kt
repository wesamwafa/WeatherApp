package com.MyWeather.data.DB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.MyWeather.data.NetworkData.Response.FavWeatherResponse

@Dao
interface FavWeatherDao {

    @Insert()
    fun insert(currentWeather: FavWeatherResponse)

    @Query("select * from fav_weather_response where id = id")
    fun getWeather(): LiveData<FavWeatherResponse>

    @Query("SELECT * FROM fav_weather_response")
    fun getListOfFavData(): LiveData<MutableList<FavWeatherResponse>>

    @Query("DELETE FROM fav_weather_response")
    fun deleteAllData()

    @Delete()
    fun deleteFavItem(favWeatherResponse: FavWeatherResponse)

}