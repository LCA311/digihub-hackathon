package de.slg.egomover

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.maps.MapView

class MainActivity : AppCompatActivity() {



    override fun onCreate(b: Bundle?) {

        super.onCreate(b)
        setContentView(R.layout.activity_main)

      //  supportFragmentManager.inTransaction {
      //      add(R.id.mapView, )
      //  }

    }
    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }


}
