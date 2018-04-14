package de.slg.egomover.fragment

import android.annotation.SuppressLint
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

class DriveFragment : Fragment() {

    private var target = ""
    private var job = Job()

    companion object {
        fun newInstance(pTarget: String) : DriveFragment  {
            val b = Bundle(1)
            b.putString("target", pTarget)
            val fragment = DriveFragment()
            fragment.arguments = b
            return fragment
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_drive, container, false)
        target = arguments!!.getString("target")
        updateTime(arrival_timestamp)
        bus_designation.text = "BUSNUMMER: ${(activity as TimeActivity).getBus().getDesignation()}"
        //TODO calculate CO2 emissions depending on distanceToTarget
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
                //TODO show progress bar

                val minutes = (activity as TimeActivity).getBus().getMinutesToTarget(target)

                //TODO hide progressbar

                async (UI) {
                    (v as TextView).text = "$minutes Minuten"
                }

                delay(60*1000)

            }
        }

    }

}
