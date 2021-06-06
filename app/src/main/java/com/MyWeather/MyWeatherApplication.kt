package com.MyWeather

import android.app.Application
import android.util.Log
import androidx.preference.PreferenceManager
import com.MyWeather.UI.Alerts.AlertsViewModelFactory
import com.MyWeather.UI.FavsFragment.FavsViewModelFactory
import com.MyWeather.UI.Weather.CurrentWeatherViewModelFactory
import com.MyWeather.data.DB.ForecastDatabase
import com.MyWeather.data.NetworkData.*
import com.MyWeather.data.repository.ForecastRepository
import com.MyWeather.data.repository.ForecastRepositoryImpl
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.util.*

class MyWeatherApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {

        import(androidXModule(this@MyWeatherApplication))

        bind() from singleton { ForecastDatabase.buildDB(instance()) }
        bind() from singleton { instance<ForecastDatabase>().getDaoInstance() }
        bind() from singleton { instance<ForecastDatabase>().getFavDaoInstance() }
        bind() from singleton { instance<ForecastDatabase>().getAlarmDaoInstance() }
        bind<ConnectionInterceptor>() with singleton { ConnectionInterceptorImpl(instance()) }
        bind() from singleton { ApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<ForecastRepository>() with singleton {
            ForecastRepositoryImpl(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
        bind() from provider { CurrentWeatherViewModelFactory(instance()) }
        bind() from provider { AlertsViewModelFactory(instance()) }
        bind() from provider { FavsViewModelFactory(instance()) }


    }


    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
       // updateResources()
    }

   
}

