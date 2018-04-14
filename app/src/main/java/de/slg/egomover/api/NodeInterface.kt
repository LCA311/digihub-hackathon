package de.slg.egomover.api

import android.util.Log
import de.slg.egomover.utility.Bus
import khttp.get

internal val ip = "http://172.26.210.84"
internal val port = 25565

data class MoverStatus(val avgSpeed: Double, val capacity: Int, val battery: Int, val designation : Int)

fun getGPS(qnr: String): Bus.GPSData {
    val response = get("$ip:$port/GPS/$qnr")

    if (response.statusCode != 200)
        return Bus.GPSData(0.0, 0.0)

    val json = response.jsonObject
    val lat = json.get("lat") as Double
    val lon = json.get("lon") as Double

    return Bus.GPSData(lat, lon)
}

fun getETC(qnr: String): MoverStatus {
    val response = get("$ip:$port/ETC/$qnr")

    if (response.statusCode != 200)
        return MoverStatus(0.0, -1, -1, -1)

    val json = response.jsonObject

    Log.d("MOVER", json.toString(2))

    val cap = json.get("capacity") as Int
    val bat = json.get("battery") as Int
    val spd = json.get("speed") as Double
    val des = json.get("designation") as Int

    return MoverStatus(spd, cap, bat, des)
}

//Returns the gps data of all available busses and an empty list if none is available or if an error occured
fun getAllGPSData() : List<Bus.GPSData> {
    val response = get("$ip:$port/AGPS")
    val ret = mutableListOf<Bus.GPSData>()

    if (response.statusCode != 200)
        return ret

    val json = response.jsonObject
    val arr = json.getJSONArray("gpsdata")

    (0..arr.length()-2)
            .map { arr.getJSONObject(it) }
            .mapTo(ret) { Bus.GPSData(it.getDouble("lat"), it.getDouble("lon")) }

    return ret

}