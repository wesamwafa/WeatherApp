package com.MyWeather.data.NetworkData

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.widget.Toast.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.MyWeather.UI.Alerts.AlertReceiver
import com.MyWeather.data.DB.Entity.Alerts
import com.MyWeather.data.NetworkData.Response.CurrentWeatherResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class WeatherWorker(context: Context, workerParams: WorkerParameters) :
        Worker(context, workerParams) {
    private var mApiHelper: ApiService? = null
    private var weatherMutableLiveDataApi: MutableLiveData<CurrentWeatherResponse> = MutableLiveData()
    private var lat: String? = null
    private var lon: String? = null
    private var lang: String? = null
    private val mCtx = context
    private var requestCodeList = ArrayList<Int>()
    private val gson = Gson()
    private lateinit var alarmManager: AlarmManager
    private lateinit var alerts: List<Alerts>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor


    override fun doWork(): Result {

        lat = inputData.getString("lat")
        lon = inputData.getString("lon")
        lang = inputData.getString("lang")
        alerts = ArrayList()
        init()
        fetchWeather()
        // val outPut = Data.Builder().putAll(weatherMutableLiveDataApi)
        return Result.success()
    }

    fun init() {
        alarmManager = mCtx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //alerts = ArrayList()
        sharedPreferences = mCtx.getSharedPreferences(
                "prefs",
                Context.MODE_PRIVATE
        )
        editor = sharedPreferences.edit()

    }

    fun setAlarm(alerts: List<Alerts>) {
        if (alerts.size > 0) {

            for (alertItem in alerts) {
                val now = System.currentTimeMillis()

                if (alertItem.start > now / 1000) {
                    setNotification(
                            alertItem.start,
                            alertItem.event,
                            alertItem.description
                    )
                }

            }
            val requestCodeJson = gson.toJson(requestCodeList)
            editor.putString("requestsOfAlerts", requestCodeJson)
            editor.commit()
            editor.apply()
        }
    }

    fun fetchWeather(): MutableLiveData<CurrentWeatherResponse> {


        GlobalScope.launch {
            Dispatchers.IO

            try {
                val response = mApiHelper!!.getCurrentWeather(
                        "68.3963", "36.9419", lang!!, "minutely",
                        API_KEY
                )
                Log.v("latvm", lat!!)
                // Check if response was successful.
                withContext(Dispatchers.Main) {

                    Log.v("wm", alerts.toString() + "hello")
                    setAlarm(ArrayList<Alerts>())


                }
            } catch (e: Exception) {
                Log.i("error", " " + e.message)
            }
        }
        return weatherMutableLiveDataApi

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun setNotification(startTime: Int, event: String, description: String) {
        val intent = Intent(mCtx, AlertReceiver::class.java)
        intent.putExtra("event", event)
        intent.putExtra("desc", description)
        val r = Random()
        val i1 = r.nextInt(99)

        val pendingIntent = PendingIntent.getBroadcast(mCtx, i1, intent, 0)
        requestCodeList.add(i1)
        val alertTime: Long = startTime.toLong()
        Log.v("alertTime", alertTime.toString())
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alertTime, pendingIntent)
        makeText(mCtx, "Aler Set", LENGTH_LONG).show()
        mCtx.registerReceiver(AlertReceiver(), IntentFilter())
    }
}