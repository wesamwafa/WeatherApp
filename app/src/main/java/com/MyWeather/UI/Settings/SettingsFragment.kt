package com.MyWeather.UI.Settings

import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.MyWeather.UI.Weather.MainActivity
import com.MyWeather.myweather.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefrences)
        findPreference<Preference>("CUSTOM_LOCATION")!!.setOnPreferenceClickListener(

            object : Preference.OnPreferenceClickListener {
                override fun onPreferenceClick(preference: Preference?): Boolean {
                    findNavController(this@SettingsFragment).navigate(R.id.action_settingsFragment_to_mapsFragmentSettings)
                    return true
                }


            })
        findPreference<Preference>("LANGUAGE_SYSTEM")!!.setOnPreferenceChangeListener (

            object : Preference.OnPreferenceChangeListener {
                override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {

                    var intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    return true
                }


            })
    }


}




