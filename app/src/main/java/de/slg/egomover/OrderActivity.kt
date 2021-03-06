package de.slg.egomover

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.slg.egomover.utility.Bus
import kotlinx.android.synthetic.main.activity_order.*
import android.util.Log
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class OrderActivity : AppCompatActivity() {

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_order)

        floatingActionButton.setOnClickListener {
            //TODO show FAB progressbar
            initializeBusTransfer()
        }

        val places = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment
        val typeFilter = AutocompleteFilter.Builder().setCountry("DE").setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).build()

        places.setFilter(typeFilter)
        places.setBoundsBias(LatLngBounds(LatLng(50.67479994045297, 5.853315170639689), LatLng(50.89186684463814, 6.320234115952189)))
        places.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                //TODO place.address --> use LatLng
            }

            override fun onError(status: Status) {
                Log.e("PlaceAutocomplete", "Error listener $status")
            }
        })


        flexible_btn.setOnClickListener {
            semi_flex_btn.isChecked = false
            on_time_btn.isChecked = false
            superspeed_btn.isChecked = false
        }
        semi_flex_btn.setOnClickListener {
            flexible_btn.isChecked = false
            on_time_btn.isChecked = false
            superspeed_btn.isChecked = false
        }
        semi_flex_btn.setOnClickListener {
            flexible_btn.isChecked = false
            on_time_btn.isChecked = false
            superspeed_btn.isChecked = false
        }
        on_time_btn.setOnClickListener {
            flexible_btn.isChecked = false
            semi_flex_btn.isChecked = false
            superspeed_btn.isChecked = false
        }
        superspeed_btn.setOnClickListener {
            flexible_btn.isChecked = false
            semi_flex_btn.isChecked = false
            on_time_btn.isChecked = false
        }

        imageButton.setOnClickListener {
            finish()
        }
    }

    private fun initializeBusTransfer() {
        //Normally we would be getting a qnr of an available bus from a server side algorithm, for demo purposes we simply simulate that behaviour
        val qnr = "30000017DC237001"

        //TODO hide FAB progressbar
        val intent = Intent(applicationContext, TimeActivity::class.java)
        intent.putExtra("bus", qnr)
        startActivity(intent)
    }
}