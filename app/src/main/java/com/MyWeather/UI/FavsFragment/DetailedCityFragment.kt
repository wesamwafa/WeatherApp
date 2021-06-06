package com.MyWeather.UI.FavsFragment

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.MyWeather.UI.Weather.DetailedFutureItemAdapter
import com.MyWeather.data.DB.Entity.Daily

import com.MyWeather.data.DB.Entity.Hourly
import com.MyWeather.data.NetworkData.Response.FavWeatherResponse
import com.MyWeather.myweather.R
import com.MyWeather.providers.GlideApp
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_detailed_city.*

import java.text.SimpleDateFormat
import java.util.*


class DetailedCityFragment : Fragment() {

    //    private var temp: Double? = null
//    private var city: String? = null
//    private var image: String? = null
//    private var humid: Int? = null
//    private var windsp: Double? = null
//    private var clouds: Int? = null
//    private var condition: String? = null
//    private var feels: Double? = null
    private lateinit var list: FavWeatherResponse
    var UNITSLOCAL = "metric"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            list = it.getParcelable<FavWeatherResponse>("favoriteitem")!!
            Log.i("SSSSSS", list.toString())
            val preferences =
                PreferenceManager.getDefaultSharedPreferences(context?.applicationContext)
            var selectedName = preferences.getString("UNIT_SYSTEM", "METRIC")
            if (selectedName == "METRIC") {
                UNITSLOCAL = "metric"
            } else {
                UNITSLOCAL = "imperial"
            }


        }


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_detailed_city, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        group_loading.visibility = View.GONE
        //Log.i("CITYYYYDETAILED", city.toString())
        print("WWWWWWWWWWW")
        createUI()

    }


    private fun createUI() {

        var geocoder =
            Geocoder(requireContext().applicationContext, Locale.getDefault())
        var city = geocoder.getFromLocation(list.lon.toDouble(), list.lat.toDouble(), 1)
        //Log.i("CITTTTYYYY", city.toString())
        var cityName = if (equals(null)) city[0].featureName else city[0].adminArea

        var tempAbbreviation = if (UNITSLOCAL.equals("metric")) "°C" else "°F"
        var speedAbbreviation = if (UNITSLOCAL.equals("metric")) getString(R.string.kmh) else getString(
                    R.string.mileh)
        textView_clouds.text = getString(R.string.clouds) + list?.current?.clouds + "%"
        textView_Cityname.text = cityName.toString()
        textView_datealarm2.text = list?.current?.weather?.get(0)?.description.toString()
        textView_feels_like_temperature.text =
            getString(R.string.feelslike)+ list?.current?.feelsLike.toString() + " " + tempAbbreviation
        textView_temperature.text = "${list?.current?.temp.toFloat()} $tempAbbreviation"
        textView_humidity.text = getString(R.string.humidity) + list?.current?.humidity.toString() + " %"
        textView_wind.text =getString(R.string.windspeed)+ list?.current?.windSpeed +speedAbbreviation
        GlideApp.with(requireContext())
            .load("https://openweathermap.org/img/wn/${list?.current?.weather?.get(0)?.icon}@4x.png")
            .apply(RequestOptions().override(600, 200))
            .into(imageView_alarm)

        var detailedhourlyweatherList = recyclerViewHour(list.hourly)

        recyclerViewDetailedHourly.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerViewDetailedHourly.hasFixedSize()
        recyclerViewDetailedHourly.adapter =
            DetailedHourlyWeatherAdapter(detailedhourlyweatherList, requireContext())

        //RecycleviewFuture
        var weatherList = createList(list.daily, list.daily.size)
        recyclerViewDetailedFuture.layoutManager =
            LinearLayoutManager(context/*,RecyclerView.HORIZONTAL,false*/)
        recyclerViewDetailedFuture.hasFixedSize()
        recyclerViewDetailedFuture.adapter =
            DetailedFutureItemAdapter(weatherList, requireContext())


    }

    fun recyclerViewHour(hourlyList: List<Hourly>): List<DetailedHourlyWeatherItem> {

        var list = ArrayList<DetailedHourlyWeatherItem>()

        for (i in 1 until 12) {

//        dailyList[size].weather[0].icon = list[size].image.toString()
            val item = DetailedHourlyWeatherItem(
                hourlyList[i].weather[0].icon, dtToTime(hourlyList[i].dt)
                , hourlyList[i].temp
            )

            list.add(item)
            //Log.i("LIIIIIST","$list")
        }
        return list


    }

    fun dtToTime(untixTimeStamp: Int): String {
        val timeFormat = SimpleDateFormat("hh:mm a")
        val date = Date()
        date.time = untixTimeStamp.toLong() * 1000
        return timeFormat.format(date)
    }

    private fun createList(dailyList: List<Daily>, size: Int): List<DetailedFutureWeatherItem> {
        var list = ArrayList<DetailedFutureWeatherItem>()

        for (i in 1 until size) {

//        dailyList[size].weather[0].icon = list[size].image.toString()
            val item = DetailedFutureWeatherItem(
                dailyList[i].weather[0].icon,
                dtToDate(dailyList[i].dt),
                dailyList[i].weather[0].description,
                dailyList[i].temp.max
            )
            list.add(item)
            //Log.i("LIIIIIST","$list")
        }
        return list
    }

    fun dtToDate(strangeNum: Int): String {
        val dateFormat = SimpleDateFormat("EEEE")
        val date = Date()
        date.time = strangeNum.toLong() * 1000
        return dateFormat.format(date)
    }


}








