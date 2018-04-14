package de.slg.egomover

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import android.graphics.drawable.Drawable
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import de.slg.egomover.api.getAllGPSData
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async


class MainActivity : AppCompatActivity(), OnMapReadyCallback {


    var googleMap: GoogleMap? = null
    val perm = 5;
    val AACHEN = LatLng(50.77580397992759, 6.091018809604975)
    val ZOOM_LEVEL = 14f


    override fun onCreate(b: Bundle?) {
        super.onCreate(b)

        setContentView(R.layout.activity_main)

        val mapFragment: SupportMapFragment? = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)  //the map is loaded asynchronously
        order_bus.setOnClickListener{
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent);
        }

      
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) { //manages and initialises fragments
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }

    override fun onMapReady(p0: GoogleMap?) {
        p0 ?: return

        googleMap = p0
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(AACHEN, ZOOM_LEVEL)) //Zoom on Aachen

        val circleDrawable = ContextCompat.getDrawable(this, R.drawable.ic_marker_bus)
        val markerIcon = getMarkerIconFromDrawable(circleDrawable!!)
        googleMap?.addMarker(MarkerOptions().position(AACHEN).icon(markerIcon).snippet("BUSNUMMER: 35"))

        enableMyLocation() //location services
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.wtf("TAG", "enableL")
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), perm)
        } else if (googleMap != null) {
            googleMap?.isMyLocationEnabled = true
        }
    }

    fun onMyLocationButtonClick(): Boolean {
        //default behaviour
        return false
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            perm -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    enableMyLocation()
                }
                //else no perm
            }
            else -> {
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

