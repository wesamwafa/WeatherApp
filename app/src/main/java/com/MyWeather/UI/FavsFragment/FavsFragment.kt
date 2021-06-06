package com.MyWeather.UI.FavsFragment

import android.app.AlertDialog
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.MyWeather.UI.Weather.ScopedFragment
import com.MyWeather.data.NetworkData.LATITUDEFAV
import com.MyWeather.data.NetworkData.LONGITUDEFAV
import com.MyWeather.data.NetworkData.Response.FavWeatherResponse
import com.MyWeather.myweather.R
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.favs_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*

class FavsFragment : ScopedFragment(), KodeinAware, FavouriteItemAdapter.onItemClick {
    override val kodein by closestKodein()
    val viewModelFactory: FavsViewModelFactory by instance()
    private var latLng: LatLng? = null
    private lateinit var favouriteItemAdapter: FavouriteItemAdapter
    private lateinit var favouriteweatherlist: List<FavWeatherResponse>
  private lateinit var viewModel: FavsViewModel
    //private lateinit var itemList: MutableList<FavouriteWeatherItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latLng = it.getParcelable("latlng")!!
            Log.i("LALALAALLA", it.toString())
            LATITUDEFAV = latLng?.latitude.toString()
            LONGITUDEFAV = latLng?.longitude.toString()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, viewModelFactory).get(FavsViewModel::class.java)

        updateUIFavRecycle()
        // ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerViewfavs)
        //refreshFragment()

        return inflater.inflate(R.layout.favs_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        refreshFragment()
        floatingActionButtonAdd.setOnClickListener(View.OnClickListener {
            findNavController(this)
                .navigate(R.id.action_favsFragment_to_mapsFragment)

        })


        updateCities()


    }

    private fun updateCities() = launch {


        if (latLng != null) {
            LATITUDEFAV = latLng!!.latitude.toString()
            LONGITUDEFAV = latLng!!.longitude.toString()
            var weather = viewModel.favsWeather.await()
        }
    }

    private fun updateUIFavRecycle() = launch {
        //DB list and item recyclerview
        viewModel.favWeatherDataList.await().observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                //itemList = createFavList(it, it.size)
                favouriteweatherlist = it

                recyclerViewfavs.layoutManager =
                    LinearLayoutManager(context/*,RecyclerView.HORIZONTAL,false*/)
                recyclerViewfavs.hasFixedSize()
                favouriteItemAdapter =
                    FavouriteItemAdapter(favouriteweatherlist, requireContext(), this@FavsFragment)
                recyclerViewfavs.adapter = favouriteItemAdapter
                ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerViewfavs)
            })

    }

    //This is only for test purposes
    private fun deleteFavDb() = GlobalScope.launch(Dispatchers.Main) {
        viewModel.favWeatherDeleteALL.await()

    }


    override fun onItemClick(position: Int) {
        //val itemClicked = itemList[position]
        favouriteItemAdapter.notifyItemChanged(position)


        var bundle = bundleOf(
            "favoriteitem" to favouriteweatherlist[position]

        )
        findNavController(this).navigate(R.id.action_favsFragment_to_detailedCityFragment, bundle)


        Log.i("APPPPP", "CLICKCKKCCKED")
    }


    fun refreshFragment() {
        srl.setOnRefreshListener {
            updateCities()
            updateUIFavRecycle()
            Toast.makeText(requireContext(), "refreshed", Toast.LENGTH_LONG)
            srl.isRefreshing = false

        }
    }

    //swipe to delete

    var itemTouchHelper: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirm")
                builder.setMessage(getString(R.string.citydelete))
                builder.setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                    updateUIFavRecycle()

                }
                builder.setPositiveButton("Yes"){dialog, which ->
                deleteFavoriteItemFromDB(favouriteItemAdapter.getItemByVH(viewHolder))
                    favouriteItemAdapter.removeItem(viewHolder as FavouriteItemAdapter.ItemViewHolder)
                }
                val alert = builder.create()
                alert.show()

            }


            //var adaptItem=  favouriteItemAdapter.itemfav(viewHolder.adapterPosition)

        }

    // delete favorite item
    fun deleteFavoriteItemFromDB(favoriteModel: FavWeatherResponse) {
        viewModel.favWeatherdelete(favoriteModel)
    }


}
