package de.slg.egomover

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import org.jetbrains.anko.toast


class OrderActivity : AppCompatActivity() {

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_order)

        val places = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment
        val typeFilter = AutocompleteFilter.Builder()
                .setCountry("DE")
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build()

        places.setFilter(typeFilter)
        places.setBoundsBias(LatLngBounds( LatLng(50.67479994045297, 5.853315170639689), LatLng(50.89186684463814, 6.320234115952189)))
        places.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                //place.address
            }

            override fun onError(status: Status) {
                Log.e("PlaceAutocomplete", "Error listener ${status}")
            }
        })

    }
}