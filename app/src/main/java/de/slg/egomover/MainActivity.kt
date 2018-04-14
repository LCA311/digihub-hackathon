package de.slg.egomover

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import de.slg.egomover.api.getAllGPSData
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class MainActivity : AppCompatActivity() {

    override fun onCreate(b : Bundle?) {

        super.onCreate(b)
        setContentView(R.layout.activity_debug)

        async (CommonPool) {
            for (data in getAllGPSData()) {
                Log.d("eGO Mover", "${data.latitude}, ${data.longitude}")
            }
        }

    }

}
