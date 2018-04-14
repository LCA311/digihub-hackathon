package de.slg.egomover

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.slg.egomover.utility.Bus
import kotlinx.android.synthetic.main.activity_order.*

class OrderActivity : AppCompatActivity() {

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_order)

        floatingActionButton.setOnClickListener {
            //TODO show FAB progressbar
            initializeBusTransfer()
        }
    }

    private fun initializeBusTransfer() {
        //Normally we would be getting a qnr of an available bus from a server side algorithm, for demo purposes we simply simulate that behaviour
        val qnr = "30000017DC237001"

        val bus = Bus(qnr, { b ->
            //TODO hide FAB progressbar
            val intent = Intent(this, TimeActivity::class.java)
            intent.putExtra("qnr", qnr)
            startActivity(intent)
        })

    }
}