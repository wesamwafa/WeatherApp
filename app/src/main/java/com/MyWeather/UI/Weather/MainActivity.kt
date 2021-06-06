package com.MyWeather.UI.Weather

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.MyWeather.data.NetworkData.LANGUAGE
import com.MyWeather.myweather.R
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import java.util.*


class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by closestKodein()


    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(toolbar)
//        actionBar?.hide()
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        bottom_nav.itemIconTintList = null
        bottom_nav.itemBackground = getDrawable(R.color.skyBlueTrans)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        );
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        bottom_nav.setBackgroundColor(Color.TRANSPARENT)

//        bottom_nav //RECHECK
        NavigationUI.setupWithNavController(bottom_nav, navController)

        //       NavigationUI.setupActionBarWithNavController(this,navController)

        requestLocationPermissions()
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this
            , arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            0
        )
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
//        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
//        val language = sharedPreferences.getString("LANGUAGE_SYSTEM", "en")
//        if (language != null) {
//            Log.i("LAAAAANGUAAAGE", language)
//        }
        //setLocale(this, LANGUAGE)

       // updateResources()

    }

    override fun attachBaseContext(newBase: Context?) {

        val sp = PreferenceManager.getDefaultSharedPreferences(newBase)
        val lang = sp.getString("LANGUAGE_SYSTEM", Locale.getDefault().language)!!
        var localeLanguage = if (lang == "ENGLISH") "en" else "ar"
        val context: Context = HelperClass.changeLang(newBase!!, Locale(localeLanguage))
        super.attachBaseContext(context)
    }

    private fun updateResources(): Boolean {


        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val language = sharedPreferences.getString("LANGUAGE_SYSTEM", "ENGLISH")
        if (language != null) {

            Log.i("LANGAAAAAUAAAAGEEEE", language)
        }
        var localeLanguage = if (language == "ENGLISH") "en" else "ar"
        Log.i("LANGAAUAAAAGEEEELOCA", localeLanguage)




        //dLocale = Locale(localeLanguage) //set any locale you want here
        return true
    }


    fun setLocale(activity: Activity, languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}