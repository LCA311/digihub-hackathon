package de.slg.egomover.fragment

import android.support.v4.app.Fragment
import android.view.View
import android.widget.TextView
import de.slg.egomover.TimeActivity
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI

abstract class TimeFragment : Fragment() {

    internal var job = Job()

    internal fun updateTime(v : View) {

        job = launch (CommonPool) {
            while (true) {

                (activity as TimeActivity).getBus().getMinutesToTarget(getTargetLatitude(), getTargetLongitude())
                delay(60*1000)

            }
        }

        async (UI) {

            val job = async (CommonPool) {

            }
            job.await()
            val tv = v as TextView
       //     tv.setText()

        }

    }

    abstract fun getTargetLatitude() : Double

    abstract fun getTargetLongitude() : Double

}
