package de.slg.egomover

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import de.slg.egomover.utility.Bus

class MainActivity : AppCompatActivity() {

    override fun onCreate(b : Bundle?) {

        super.onCreate(b)
        setContentView(R.layout.activity_debug)

        val bus = Bus("30000017DC237001", { bus ->
            Log.d("eGO Mover", bus.getDesignation().toString())
        })

        bus.stopKeepUpDate()

    }

}
