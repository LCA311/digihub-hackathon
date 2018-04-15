package de.slg.egomover.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.slg.egomover.R
import de.slg.egomover.TimeActivity
import kotlinx.android.synthetic.main.fragment_drive.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.util.Log
import kotlinx.android.synthetic.main.fragment_time.view.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import de.slg.egomover.MainActivity
import de.slg.egomover.api.getAllGPSData
import de.slg.egomover.utility.Bus
import kotlinx.android.synthetic.main.fragment_drive.view.*


class WaitFragment : Fragment(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var latitude = 0.0
    private var longitude = 0.0
    private var job = Job()
    private val AACHEN = LatLng(50.77580397992759, 6.091018809604975)
    private val ZOOM_LEVEL = 14f

    companion object {
        fun newInstance(): WaitFragment = WaitFragment()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_time, container, false)
        setOwnGPSData()
        val mapFragment: SupportMapFragment? = activity?.supportFragmentManager?.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)  //the map is loaded asynchronously

        updateTime(v.arrival_timestamp_time)
        v.busnummer_time.text = "BUSNUMMER: 15" //TODO get from bus after sync
        return v
    }


    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onMapReady(p0: GoogleMap?) {
        p0 ?: return

        mMap = p0
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(AACHEN, ZOOM_LEVEL)) //Zoom on Aachen

        val circleDrawable = ContextCompat.getDrawable(context!!, R.drawable.ic_marker_bus)
        val markerIcon = getMarkerIconFromDrawable(circleDrawable!!)

        //  mMap?.addMarker(MarkerOptions().position(AACHEN).icon(markerIcon).snippet("Passgiere: 11").title("BUSNUMMER: 35")) //MARKER, testing purposes in Aachen
        var busGPS: Bus.GPSData? = (activity as TimeActivity).getBus().getGPS()
        var l = LatLng(busGPS!!.latitude, busGPS!!.longitude)
        mMap?.addMarker(MarkerOptions().position(l).icon(markerIcon).title("BUSNUMMER: 17").snippet("Passagiere: 7")) //Hard-coded. Will be changed

        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(l, ZOOM_LEVEL)) //Zoom on Aachen


    }


    private fun setOwnGPSData() {
        val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 0)
        }

        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        latitude = location.latitude
        longitude = location.longitude

        Log.d("MOVER", latitude.toString())
        Log.d("MOVER", longitude.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun updateTime(v: View) {

        job = launch(CommonPool) {
            while (true) {

                val minutes = (activity as TimeActivity).getBus().getMinutesToTarget(latitude, longitude)

                async(UI) {
                    if (minutes == 1) {  //Normally minutes == 0, but in our presentation route, the smallest time is 1
                        (this@WaitFragment.activity as TimeActivity).switchToDriveMode()
                        job.cancel()
                    } else {
                        (v as TextView).text = "$minutes Minuten"
                    }
                }

                delay(10 * 1000)

            }
        }

    }

    private fun getMarkerIconFromDrawable(drawable: Drawable): BitmapDescriptor {
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}

