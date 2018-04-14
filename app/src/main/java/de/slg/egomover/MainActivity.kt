package de.slg.egomover

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import de.slg.egomover.utility.Bus

class MainActivity : AppCompatActivity() {

    override fun onCreate(b : Bundle?) {

        super.onCreate(b)
        setContentView(R.layout.activity_debug)

        val eta = Bus("stub").getMinutesToTarget(50.77305680000001, 6.0830810999999585)
        Log.d("eGOMover", eta.toString())

    }

}
