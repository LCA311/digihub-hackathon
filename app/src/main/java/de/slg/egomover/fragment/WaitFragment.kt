package de.slg.egomover.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.slg.egomover.R
import kotlinx.android.synthetic.main.fragment_drive.*

class WaitFragment : TimeFragment() {

    private var latitude = 0.0
    private var longitude = 0.0

    init {
        //TODO get own geolocation
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

    override fun getTargetLatitude(): Double = latitude

    override fun getTargetLongitude(): Double = longitude

}
