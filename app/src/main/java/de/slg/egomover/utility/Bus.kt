package de.slg.egomover.utility

import de.slg.egomover.api.*
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

    //Time of last synchronization process
    private var lastSync = Date().time

    init { //Sync once on creation of bus object
        geolocation = de.slg.egomover.api.getGPS(id)
        syncETC()
    }

    fun getId() : String {
        return id
    }

    fun getGPS() : GPSData {
        if (isSyncNecessary(true))
            geolocation = de.slg.egomover.api.getGPS(id)

        return geolocation
    }

    fun getBattery() : Int {
        syncETC()
        return batteryLevel
    }

    fun getCurrentCapacity() : Int {
        syncETC()
        return capacity
    }

    fun getAverageSpeed() : Double {
        syncETC()
        return avgSpeed
    }

    fun getMinutesToTarget(latitude: Double, longitude: Double) : Int {
        if (GPSData(latitude, longitude) != lastTarget || isSyncNecessary(true)) {
            getGPS(id)
            eta = getETA(geolocation, GPSData(latitude, longitude))
        }

        return eta
    }

    private fun isSyncNecessary(isGPSSync : Boolean) : Boolean {
        return if (isGPSSync) Date().time - lastSync > 60*1000 else Date().time - lastSync > 60*1000*5
    }

    private fun syncETC() {
        if (isSyncNecessary(false)) {
            val status = getETC(id)
            avgSpeed = status.avgSpeed
            capacity = status.capacity
            batteryLevel = status.battery
        }
    }

}