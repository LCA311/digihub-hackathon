package de.slg.egomover.utility

import de.slg.egomover.api.*
import kotlinx.coroutines.experimental.*
import java.util.*

/**
 * Bus.
 *
 * Stores data about a specific bus identified by a unique "qnr". Data is updated regularly depending
 * on priority.
 *
 * @version 2018.1304
 */
class Bus constructor(private var id : String) {

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


    private var etcJob = Job()
    private var gpsJob = Job()

    init { //Sync once on creation of bus object
        startKeepUpToDate()
    }

    private fun startKeepUpToDate() {
        etcJob = launch (CommonPool) {
            while (true) {
                val status = getETC(id)
                avgSpeed = status.avgSpeed
                capacity = status.capacity
                batteryLevel = status.battery
                delay(5*60*1000)
            }
        }

        gpsJob = launch (CommonPool) {
            while (true) {
                geolocation = getGPS(id)
                delay(60*1000)
            }
        }
    }

    fun stopKeepUpDate() {
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

    //Must be called asynchronously
    fun getMinutesToTarget(latitude: Double, longitude: Double) : Int {
        if (GPSData(latitude, longitude) != lastTarget) {
            geolocation = getGPS(id)
            eta = getETA(geolocation, GPSData(latitude, longitude))
        }

        return eta
    }

}