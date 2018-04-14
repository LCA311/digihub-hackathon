package de.slg.egomover

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.slg.egomover.fragment.DriveFragment
import de.slg.egomover.fragment.WaitFragment
import de.slg.egomover.utility.Bus

class TimeActivity : AppCompatActivity() {

    private var currentBus : Bus? = null
    private var enteredTarget = "Viktoriaallee 60, 52066 Aachen"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)

        //TODO enable progressbar
        currentBus = Bus(intent.getStringExtra("bus"), {
            //TODO disable progressbar
        })

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragments, WaitFragment.newInstance())
                    .commit()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        currentBus?.stopKeepUpToDate()
    }

    fun switchToDriveMode() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragments, DriveFragment.newInstance(enteredTarget))
                .commit()
    }

    fun getBus() : Bus = currentBus!!

}
