package de.slg.egomover.api

import de.slg.egomover.utility.Bus

val maps = "http://maps.googleapis.com/maps/api/directions/json"

fun getETA(source : Bus.GPSData, destination : Bus.GPSData) : Int {

    val response = khttp.get("$maps?origin=${source.latitude},${source.longitude}" +
            "&destination=${destination.latitude},${destination.longitude}" +
            "&key=AIzaSyBhlQYYdteO1iXgSkO81BXGFGFNj3SOzik")

    if (response.statusCode != 200)
        return -1

    val json = response.jsonObject
    val minutes = json.getJSONArray("routes")
            .getJSONObject(0)
            .getJSONArray("legs")
            .getJSONObject(0)
            .getJSONObject("duration")
            .getString("text")

    return minutes.split(" ")[0].toInt()
}