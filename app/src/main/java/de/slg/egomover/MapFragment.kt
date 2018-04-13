package de.slg.egomover

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import de.slg.egomover.R.id.mapView
import kotlinx.android.synthetic.main.activity_main.*

class MapFragment : Fragment(), OnMapReadyCallback{

    var mMapView: MapView? = null
    var map: GoogleMap? = null
    override fun onCreate(b: Bundle?) {

        super.onCreate(b)
        mMapView = mapView

    }
    public override fun onResume() {
        super.onResume()
        if (mMapView != null) {
            mMapView!!.onResume()
        }
    }

    public override fun onPause() {
        if (mMapView != null) {
            mMapView!!.onPause()
        }
        super.onPause()
    }

    public override fun onDestroy() {
        if (mMapView != null) {
            try {
                mMapView!!.onDestroy()
            } catch (e: NullPointerException) {
                Log.e("msg", "Error MapView.onDestroy()", e)
            }

        }
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        if (mMapView != null) {
            mMapView!!.onLowMemory()
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mMapView != null) {
            mMapView!!.onSaveInstanceState(outState)
        }
    }


    override fun onMapReady(p0: GoogleMap?) {
        map = p0
        map!!.getUiSettings().setMyLocationButtonEnabled(false)
      //  map!!.setMyLocationEnabled(true)


        map!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(43.1, -87.0)))

    }

}