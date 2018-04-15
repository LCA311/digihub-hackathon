package de.slg.egomover.api

import android.util.Log
import de.slg.egomover.utility.Bus
import java.net.URLEncoder

val maps = "https://maps.googleapis.com/maps/api/directions/json"

data class TravelDetails(val eta: Int, val kilometers : Double)

fun getETA(source : Bus.GPSData, destination : Bus.GPSData) : TravelDetails {

    val response = khttp.get("$maps?origin=${source.latitude},${source.longitude}" +
            "&destination=${destination.latitude},${destination.longitude}" +
            "&key=AIzaSyBhlQYYdteO1iXgSkO81BXGFGFNj3SOzik")

    Log.d("MOVER", response.text)

    if (response.statusCode != 200)
        return TravelDetails(-1, -1.0)

    val json = response.jsonObject
    val minuteString = json.getJSONArray("routes")
            .getJSONObject(0)
            .getJSONArray("legs")
            .getJSONObject(0)
            .getJSONObject("duration")
            .getString("text")

    val distanceString = json.getJSONArray("routes")
            .getJSONObject(0)
            .getJSONArray("legs")
            .getJSONObject(0)
            .getJSONObject("distance")
            .getString("text")

    val minutes = minuteString.split(" ")[0].toInt()
    val distance = distanceString.split(" ")[0].toDouble()

    return TravelDetails(minutes, distance)
}

fun getETA(source : Bus.GPSData, destination : String) : TravelDetails {

    val response = khttp.get("$maps?origin=${source.latitude},${source.longitude}" +
            "&destination=${URLEncoder.encode(destination, "UTF-8")}" +
            "&key=AIzaSyBhlQYYdteO1iXgSkO81BXGFGFNj3SOzik")

    if (response.statusCode != 200)
        return TravelDetails(-1, -1.0)

    val json = response.jsonObject
    val minuteString = json.getJSONArray("routes")
            .getJSONObject(0)
            .getJSONArray("legs")
            .getJSONObject(0)
            .getJSONObject("duration")
            .getString("text")

    val distanceString = json.getJSONArray("routes")
            .getJSONObject(0)
            .getJSONArray("legs")
            .getJSONObject(0)
            .getJSONObject("distance")
            .getString("text")

    val minutes = minuteString.split(" ")[0].toInt()
    val distance = distanceString.split(" ")[0].toDouble()

    return TravelDetails(minutes, distance)
}
