package com.MyWeather.UI.Weather

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.MyWeather.UI.Alerts.AlertReceiver
import com.MyWeather.data.DB.Entity.Daily
import com.MyWeather.data.DB.Entity.Hourly
import com.MyWeather.data.NetworkData.LANGUAGE
import com.MyWeather.data.NetworkData.LATITUDE
import com.MyWeather.data.NetworkData.LONGITUDE
import com.MyWeather.myweather.R
import com.MyWeather.providers.GlideApp
import com.MyWeather.data.NetworkData.WeatherWorker
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    val viewModelFactory: CurrentWeatherViewModelFactory by instance()
    var UNITSLOCAL = "metric"
    var selectedAlerts: Boolean = false
    private lateinit var alarmManager: AlarmManager


    lateinit var fusedLocationClient: FusedLocationProviderClient


    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setUpFetchFromApiWorker()

        alarmManager =
                requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // init()
        return inflater.inflate(R.layout.current_weather_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
                ViewModelProvider(this, viewModelFactory).get(CurrentWeatherViewModel::class.java)
        group_loading.visibility = View.GONE
        //view?.background= getDrawable(requireContext(),R.drawable.background)
        init()
    }

    override fun onStart() {
        super.onStart()
        group_loading.visibility = View.GONE

        init()
    }

    override fun onResume() {
        super.onResume()
        group_loading.visibility = View.GONE

        init()

    }


    private fun init() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context?.applicationContext)

        var selectedName = preferences.getString("UNIT_SYSTEM", "METRIC")
        var selectedLang = preferences.getString("LANGUAGE_SYSTEM", "ENGLISH")
        selectedAlerts = preferences.getBoolean("WEATHER_ALERTS", false)
        Log.i("PREFRENCESSSS", selectedName.toString())

        var location = preferences.getBoolean("USE_DEVICE_LOCATION", true)
        if (location) {
            setCurrentLocation()
        }

        if (selectedName == "METRIC") {
            UNITSLOCAL = "metric"
        } else {
            UNITSLOCAL = "imperial"
        }
        if (selectedLang == "ENGLISH") {
            LANGUAGE = "en"
        } else {
            LANGUAGE = "ar"
        }
        //Log.i("LANGUAGE", LANGUAGE)

        updateUI()


    }

    @SuppressLint("FragmentLiveDataObserve")
    fun updateUI() = launch {
        var weather = viewModel.weather.await()
        weather.observe(this@CurrentWeatherFragment, Observer {
            if (it == null) return@Observer

            var geocoder =
                    Geocoder(requireContext().applicationContext, Locale.getDefault())
            var city = geocoder.getFromLocation(LATITUDE.toDouble(), LONGITUDE.toDouble(), 1)
            Log.i("CITTTTYYYY", city.toString())
            var cityName = if (equals(null)) city[0].featureName else city[0].adminArea
            var tempAbbreviation = if (UNITSLOCAL.equals("metric")) "°C" else "°F"
            var speedAbbreviation = if (UNITSLOCAL.equals("metric")) getString(R.string.kmh) else getString(R.string.mileh)
            textView_temperature.text =
                    if (UNITSLOCAL == "metric") "${it.current.temp} $tempAbbreviation" else "${((it.current.temp * 9 / 5) + 32).toFloat()} $tempAbbreviation"
            textView_feels_like_temperature.text =
                    if (UNITSLOCAL == "metric")
                        getString(R.string.feelslike) + it.current.feelsLike + " " + tempAbbreviation else "${((it.current.feelsLike * 9 / 5) + 32).toFloat()} $tempAbbreviation"
            textView_Cityname.text = cityName
            textView_datealarm2.text = it.current.weather.component1().description
            textView_humidity.text = getString(R.string.humidity) + it.current.humidity + " %"
            textView_wind.text =
                    if (UNITSLOCAL == "metric") getString(R.string.windspeed) + it.current.windSpeed + speedAbbreviation else getString(R.string.windspeed) + (it.current.windSpeed * 0.6214).toFloat() + speedAbbreviation
            textView_pressure.text = getString(R.string.pressure) + it.current.pressure + " hPa"
            textView_clouds.text = getString(R.string.clouds) + it.current.clouds + "%"
            GlideApp.with(requireContext())
                    .load("https://openweathermap.org/img/wn/${it.current.weather.component1().icon}@4x.png")
                    .apply(RequestOptions().override(600, 200))
                    .into(imageView_alarm)
            //Log.i("ICOOOOONC", it.current.weather.component1().icon)

            //RecyclerViewHourly

            var hourlyweatherList = recyclerViewHour(it.hourly)

            recyclerViewHourly.layoutManager =
                    LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            recyclerViewHourly.hasFixedSize()
            recyclerViewHourly.adapter = HourlyWeatherAdapter(hourlyweatherList, requireContext())

            //RecycleviewFuture
            var weatherList = createList(it.daily, it.daily.size)
            recyclerViewFuture.layoutManager =
                    LinearLayoutManager(context/*,RecyclerView.HORIZONTAL,false*/)
            recyclerViewFuture.hasFixedSize()
            recyclerViewFuture.adapter = FutureItemAdapter(weatherList, requireContext())


            if (selectedAlerts == true) {
                println("ENTERD HERE 1")
                if (it.alerts!!.isNotEmpty()) {
                    println("ENTERD HERE 2")
                    val intentA = Intent(context, AlertReceiver::class.java)
                    println(it.alerts!![0].description)
                    intentA.putExtra("description", it.alerts!![0].description)
                    val r = Random()
                    val i1 = r.nextInt(99)
                    val pendingIntentA = PendingIntent.getBroadcast(context, i1, intentA, 0)
                    val calendar = Calendar.getInstance()

                    var alarmtime: Long = it.alerts!![0].start.toLong()

                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmtime, pendingIntentA)
                    Toast.makeText(context, "Alert set", Toast.LENGTH_LONG).show()
                    requireActivity().registerReceiver(AlertReceiver(), IntentFilter())

                }


            }
//

        })
    }

    fun recyclerViewHour(hourlyList: List<Hourly>): List<HourlyWeatherItem> {

        var list = ArrayList<HourlyWeatherItem>()

        for (i in 1 until 12) {

//        dailyList[size].weather[0].icon = list[size].image.toString()
            val item = HourlyWeatherItem(
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

    @SuppressLint("MissingPermission")
    fun setCurrentLocation() {

        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    LATITUDE = location?.latitude.toString()
                    Log.i("LATI", LATITUDE)
                    LONGITUDE = location?.longitude.toString()
                    Log.i("LONGI", LONGITUDE)
                }


    }

    private fun createList(dailyList: List<Daily>, size: Int): List<FutureWeatherItem> {
        var list = ArrayList<FutureWeatherItem>()

        for (i in 1 until size) {

//        dailyList[size].weather[0].icon = list[size].image.toString()
            val item = FutureWeatherItem(
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

    private fun setUpAlerts() {


        if (selectedAlerts) {
            setUpFetchFromApiWorker()

//        } else {
//            val requestCodeListJson = preferences.getString("requestsOfAlerts", " ")
//            val type: Type = object : TypeToken<List<Int>>() {}.type
//            val requestCodeList: List<Int> = Gson().fromJson(requestCodeListJson, type)
//
//            for (requestCodeItem in requestCodeList) {
//
//                cancelAlarm(requestCodeItem)
//            }

        }
    }

    fun cancelAlarm(requestCode: Int) {
        val intent = Intent(requireContext(), AlertReceiver::class.java)
        val sender = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }

    private fun setUpFetchFromApiWorker() {
        val workManager = WorkManager.getInstance()
        val data: Data = Data.Builder().build()
        val constrains = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val repeatingRequest = PeriodicWorkRequest.Builder(
                WeatherWorker::class.java, 1,
                TimeUnit.HOURS)
                .setConstraints(constrains)
                .setInputData(data)
                .build()
        workManager.enqueue(repeatingRequest)
        workManager.getWorkInfoByIdLiveData(repeatingRequest.id).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
        })
    }

}


//@SuppressLint("SimpleDateFormat")
//@BindingAdapter("displayDate")
//public fun unixTimeStampToDate(textView: TextView,untixTimeStamp:Int):String{
//    val dateFormat=SimpleDateFormat("dd/MM/yyyy")
//    val date= Date()
//    date.time=untixTimeStamp.toLong()*1000
//    textView.text = dateFormat.format(date)
//    return dateFormat.format(date)
//}
///**
// * convert unix time to time
// */
//@SuppressLint("SimpleDateFormat")
//@BindingAdapter("displayTime")
//public fun unixTimeStampToTime(textView: TextView,untixTimeStamp:Int):String{
//    val timeFormat=SimpleDateFormat("HH:mm")
//    val date= Date()
//    date.time=untixTimeStamp.toLong()*1000
//    textView.text = timeFormat.format(date)
//    return timeFormat.format(date)
//}


