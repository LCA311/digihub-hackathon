package de.slg.egomover.utility

import de.slg.egomover.api.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import java.io.Serializable
import java.util.*

/**
 * Bus.
 *
 * Stores data about a specific bus identified by a unique "qnr". Data is updated regularly depending
 * on priority.
 *
 * @version 2018.1304
 */
class Bus constructor(private var id : String, private val callback : (b : Bus) -> Unit) {

    //TODO: regularly sync values or sync on demand

    data class GPSData(val latitude : Double, val longitude : Double)

    //Maximum bus passenger capacity
    private val maxCapacity = 20

    //Current gps position of eGO Mover
    private var geolocation = GPSData(0.0, 0.0)
    //Last target that was checked via getMinutesToTarget()
    private var lastTarget = GPSData(0.0, 0.0)
    //Bus passenger capacity
    private var capacity = maxCapacity
    //Current battery level
    private var batteryLevel = 100
    //Avg Speed
    private var avgSpeed = 0.0
    //Last measured eta to lastTarget
    private var eta = 0
    //Last measured distance to lastTarget
    private var distance = 0.0
    //Reader-friendly bus designation
    private var designation = -1


    private var etcJob = Job()
    private var gpsJob = Job()

    init { //Sync once on creation of bus object
        startKeepUpToDate()
    }

    private fun startKeepUpToDate() {
        etcJob = launch (CommonPool) {

            val initialStatus = getETC(id)
            avgSpeed = initialStatus.avgSpeed
            capacity = initialStatus.capacity
            designation = initialStatus.designation
            batteryLevel = initialStatus.battery
            geolocation = getGPS(id)

            async (UI) {
                callback(this@Bus)
            }

            while (true) {

                delay(5*60*1000)

                val status = getETC(id)
                avgSpeed = status.avgSpeed
                capacity = status.capacity
                designation = status.designation
                batteryLevel = status.battery
            }
        }

        gpsJob = launch (CommonPool) {
            while (true) {
                delay(10*1000) //TODO Interpolate between GPS steps
                geolocation = getGPS(id)
            }
        }
    }

    fun stopKeepUpToDate() {
        gpsJob.cancel()
        etcJob.cancel()
    }

    fun getId() : String {
        return id
    }

    fun getGPS() : GPSData {
        return geolocation
    }

    fun getBattery() : Int {
        return batteryLevel
    }

    fun getCurrentCapacity() : Int {
        return capacity
    }

    fun getAverageSpeed() : Double {
        return avgSpeed
    }

    fun getDesignation() : Int {
        return designation
    }

    //Must be called asynchronously
    fun getMinutesToTarget(latitude: Double, longitude: Double) : Int {
        if (GPSData(latitude, longitude) != lastTarget) {
            geolocation = getGPS(id)
            eta = getETA(geolocation, GPSData(latitude, longitude)).eta
        }

        return eta
    }

    //Must be called asynchronously
    fun getDistanceToTarget(latitude: Double, longitude: Double) : Double {
        if (GPSData(latitude, longitude) != lastTarget) {
            geolocation = getGPS(id)
            distance = getETA(geolocation, GPSData(latitude, longitude)).kilometers
        }

        return distance
    }

    //Must be called asynchronously
    fun getMinutesToTarget(target: String) : Int {
        //geolocation = getGPS(id)
        eta = getETA(geolocation, target).eta
        return eta
    }

    //Must be called asynchronously
    fun getDistanceToTarget(target: String) : Double {
        //geolocation = getGPS(id)
        distance = getETA(geolocation, target).kilometers
        return distance
    }

}