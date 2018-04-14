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
import android.location.LocationManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import kotlinx.android.synthetic.main.fragment_time.view.*


class WaitFragment : Fragment() {

    private var latitude = 0.0
    private var longitude = 0.0
    private var job = Job()

    companion object {
        fun newInstance() : WaitFragment = WaitFragment()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_time, container, false)
        setOwnGPSData()
        updateTime(v.arrival_timestamp_time)
        v.busnummer_time.text = "BUSNUMMER: 15" //TODO get from bus after sync
        return v
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    private fun setOwnGPSData() {
        val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if ( ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 0)
        }

        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        latitude = location.latitude
        longitude = location.longitude

        Log.d("MOVER", latitude.toString())
        Log.d("MOVER", longitude.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun updateTime(v : View) {

        job = launch (CommonPool) {
            while (true) {

                val minutes = (activity as TimeActivity).getBus().getMinutesToTarget(latitude, longitude)

                async (UI) {
                    if (minutes == 1) //Normally minutes == 0, but in our presentation route, the smallest time is 1
                        (this@WaitFragment.activity as TimeActivity).switchToDriveMode()
                    else
                        (v as TextView).text = "$minutes Minuten"
                }

                delay(10*1000)

            }
        }

    }

}
