package com.MyWeather.data.NetworkData

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class ConnectionInterceptorImpl(context : Context)
    : ConnectionInterceptor {

    private val appcontext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
    if(!isOnline())
        throw  IOException()
        return chain.proceed(chain.request())
    }

    private fun isOnline() : Boolean{


        val connectivityManager =
            appcontext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?


        val network = connectivityManager!!.activeNetwork
        val networkCapabilities =
            connectivityManager!!.getNetworkCapabilities(network)


        if (networkCapabilities == null) {
            Log.d("DataMobile", "No network capabilities found")
            return false
        }


        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    //
//    private fun isOnline():Boolean{
//
//        val connectivityManager = appcontext.getSystemService(Context.CONNECTIVITY_SERVICE)
//        as ConnectivityManager
//        val networkInfo = connectivityManager.activeNetworkInfo
//        return networkInfo != null && networkInfo.isConnected
//    }
}