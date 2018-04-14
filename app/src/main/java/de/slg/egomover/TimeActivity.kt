package de.slg.egomover

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.slg.egomover.fragment.DriveFragment
import de.slg.egomover.fragment.WaitFragment
import de.slg.egomover.utility.Bus

class TimeActivity : AppCompatActivity() {

    private var currentBus : Bus? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragments, WaitFragment.newInstance())
                    .commit()
        }

    }

    fun switchToDriveMode() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragments, DriveFragment.newInstance())
                .commit()
    }

    fun getBus() : Bus = currentBus!!

}
