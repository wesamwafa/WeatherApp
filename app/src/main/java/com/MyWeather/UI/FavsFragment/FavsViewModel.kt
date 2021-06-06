package com.MyWeather.UI.FavsFragment

import androidx.lifecycle.ViewModel
import com.MyWeather.data.NetworkData.Response.FavWeatherResponse
import com.MyWeather.data.repository.ForecastRepository
import com.MyWeather.internal.lazyDeferred

class FavsViewModel(private val forecastRepository: ForecastRepository) : ViewModel() {

    val favsWeather by lazyDeferred {
        forecastRepository.getFavWeather()
    }
    val favWeatherDataList by lazyDeferred {
        forecastRepository.getAllFavData()
    }

    val favWeatherDeleteALL by lazyDeferred {
        forecastRepository.deleteAllData()
    }
//    val favWeatherItemDelete by lazyDeferred {
//        //forecastRepository.deleteFavItem()
//    }

     fun favWeatherdelete(favouriteWeatherResponse:FavWeatherResponse ){
        forecastRepository.deleteFavItem(favouriteWeatherResponse)
    }


}