package de.slg.egomover.api

import de.slg.egomover.utility.Bus
import khttp.get

internal val ip = "10:dd:b1:f1:65:7e"
internal val port = 25565

data class MoverStatus(val avgSpeed : Double, val capacity : Int, val battery : Int)

fun getGPS(qnr : String) : Bus.GPSData {
    val response = get("${ip}:${port}/GPS/$qnr")

    if (response.statusCode != 200)
        return Bus.GPSData(0.0, 0.0)

    val json = response.jsonObject
    val lat = json.get("lat") as Double
    val lon = json.get("lon") as Double

    return Bus.GPSData(lat, lon)
}

fun getETC(qnr : String) : MoverStatus {
    val response = get("${ip}:${port}/ETC/$qnr")

    if (response.statusCode != 200)
        return MoverStatus(0.0, -1, -1)

    val json = response.jsonObject
    val cap = json.get("cap") as Int
    val bat = json.get("bat") as Int
    val spd = json.get("spd") as Double

    return MoverStatus(spd, cap, bat)
}