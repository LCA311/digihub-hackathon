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


class WaitFragment : Fragment() {

    private var latitude = 0.0
    private var longitude = 0.0
    private var job = Job()

    init {
        val locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if ( ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 0)
        }

        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        latitude = location.latitude
        longitude = location.longitude
    }

    companion object {
        fun newInstance() : WaitFragment = WaitFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_drive, container, false)
        updateTime(arrival_timestamp)
        return v
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    @SuppressLint("SetTextI18n")
    private fun updateTime(v : View) {

        job = launch (CommonPool) {
            while (true) {

                val minutes = (activity as TimeActivity).getBus().getMinutesToTarget(latitude, longitude)

                async (UI) {
                    if (minutes == 0)
                        (this@WaitFragment.activity as TimeActivity).switchToDriveMode()
                    else
                        (v as TextView).text = "$minutes Minuten"
                }

                delay(60*1000)

            }
        }

    }

}
