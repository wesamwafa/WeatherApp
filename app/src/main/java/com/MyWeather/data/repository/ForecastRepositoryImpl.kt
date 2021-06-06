package com.MyWeather.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.MyWeather.data.DB.AlarmsDao
import com.MyWeather.data.DB.CurrentWeatherResponseDao
import com.MyWeather.data.DB.Entity.Alarms
import com.MyWeather.data.DB.Entity.Alerts
import com.MyWeather.data.DB.Entity.Current
import com.MyWeather.data.DB.FavWeatherDao
import com.MyWeather.data.NetworkData.*
import com.MyWeather.data.NetworkData.Response.CurrentWeatherResponse
import com.MyWeather.data.NetworkData.Response.FavWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime


class ForecastRepositoryImpl(
    val currentWeatherResponseDao: CurrentWeatherResponseDao,
    val weatherNetworkDataSource: WeatherNetworkDataSource,
    val favWeatherDao: FavWeatherDao,
    val alarmsDao: AlarmsDao
) : ForecastRepository {



    init {
        weatherNetworkDataSource.apply {
            downloadedCurrentWeather.observeForever { newCurrentWeather ->
                if (newCurrentWeather.alerts == null) {
                    var alertList = arrayListOf<Alerts>()
                    newCurrentWeather.alerts = alertList
                    //Log.i("CURRREENENT", newCurrentWeather.alerts.toString())
                }

                persistFetchedCurrentWeather(newCurrentWeather)

            }
            downloadedFavWeather.observeForever { newfavWeather ->
                persistFetchedFavWeather(newfavWeather)
            }
        }
    }

    override suspend fun getCurrentWeather(): LiveData<out CurrentWeatherResponse> {
        return withContext(Dispatchers.IO) {
            initWeatherDataCurrent()
            return@withContext currentWeatherResponseDao.getWeather()

        }
    }

    override suspend fun getFavWeather(): LiveData<out FavWeatherResponse> {
        return withContext(Dispatchers.IO) {
            initWeatherDataFav()
            return@withContext favWeatherDao.getWeather()

        }
    }


    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherResponseDao.upsert(fetchedWeather)

        }
    }

    private fun persistFetchedFavWeather(fetchedWeather: FavWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            favWeatherDao.insert(fetchedWeather)

        }
    }

    private suspend fun initWeatherDataCurrent() {
        if (isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1))) {
            fetchedCurrentWeather()
        }

    }

    private suspend fun initWeatherDataFav() {
        if (isFetchFavNeeded(ZonedDateTime.now().minusHours(1))) {
            fetchedFavWeather()
        }
    }

    private suspend fun fetchedCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(LATITUDE, LONGITUDE, LANGUAGE, API_KEY, EXCLUDE)
    }

    private suspend fun fetchedFavWeather() {
        weatherNetworkDataSource.fetchFavWeather(LATITUDEFAV, LONGITUDEFAV, LANGUAGE, API_KEY, EXCLUDE)
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {

        val tenMinutesAgo = ZonedDateTime.now().minusMinutes(10)
        return lastFetchTime.isBefore(tenMinutesAgo)
    }

    private fun isFetchFavNeeded(lastFetchTime: ZonedDateTime): Boolean {

        val tenMinutesAgo = ZonedDateTime.now().minusMinutes(10)
        return lastFetchTime.isBefore(tenMinutesAgo)
    }

    override suspend fun getAllFavData(): LiveData<MutableList<FavWeatherResponse>> {
        return favWeatherDao.getListOfFavData()
    }

    override suspend fun deleteAllData() {
        favWeatherDao.deleteAllData()
    }

    override fun deleteFavItem(favWeatherDelete: FavWeatherResponse) {
        favWeatherDao.deleteFavItem(favWeatherDelete)
    }

    override fun addAlarm(alarms: Alarms) {
        alarmsDao.insert(alarms)
    }

    override fun getAlarms(): LiveData<MutableList<Alarms>> {
        return alarmsDao.getListOfAlarms()
    }

    override fun deleteAlarm(alarms: Alarms) {

        alarmsDao.deleteAlarmItem(alarms)
    }
}