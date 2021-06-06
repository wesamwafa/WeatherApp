package com.MyWeather.UI.FavsFragment

import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.MyWeather.data.NetworkData.LATITUDEFAV

import com.MyWeather.data.NetworkData.LONGITUDEFAV
import com.MyWeather.myweather.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private lateinit var googleMap: GoogleMap
    private lateinit var geocoder: Geocoder

    companion object {
        private val TAG = "FAVMAPS"
    }

//    private val callback = OnMapReadyCallback { googleMap ->
//
//        val sydney = LatLng(-34.0, 151.0)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        geocoder = Geocoder(context)
    }

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map
        googleMap.setOnMapClickListener(this)
        try {

            val addresses = geocoder.getFromLocationName("Egypt", 1)
            if (addresses.size > 0) {
                val lond = LatLng(addresses.get(0).latitude, addresses.get(0).longitude)
                map.addMarker((MarkerOptions().position(lond).title(addresses.get(0).locality)))
                map.moveCamera(CameraUpdateFactory.newLatLng(lond))

            } else
                Toast.makeText(requireActivity(), "Invalid Place", Toast.LENGTH_LONG)
        } catch (e: IOException) {
            Log.i("MAP", "NO Connection ")
            Toast.makeText(requireActivity(), "no Internet", Toast.LENGTH_LONG)


        }
    }

    override fun onMapClick(latlng: LatLng) {
        try {

            val addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1)
            if (addresses.size > 0) {
                val address = addresses.get(0)
                val strtaddress = address.getAddressLine(0)
                googleMap.addMarker(MarkerOptions().position(latlng).title(strtaddress))
                val latlonBundle = bundleOf("latlng" to latlng)
                findNavController(this).navigate(R.id.action_mapsFragment_to_favsFragment, latlonBundle)
            } else {

                Toast.makeText(requireActivity(), "Invalid Address", Toast.LENGTH_LONG)

            }
        } catch (e: IOException) {
            Log.i("MAP", "NO Connection ")
            Toast.makeText(requireActivity(), "no Internet", Toast.LENGTH_LONG)


        }


    }
}