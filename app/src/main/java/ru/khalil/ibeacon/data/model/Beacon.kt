package ru.khalil.ibeacon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.altbeacon.beacon.Beacon

@Entity(tableName = "beacons")
data class Beacon(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val uuid: String,
    val major: String,
    val minor: String,
    val rssi: Int,
    val distance: Double
) {
    companion object{
        operator fun invoke(beacon: Beacon) = Beacon(
                uuid = beacon.id1.toString(),
                major = beacon.id2.toString(),
                minor =  beacon.id3.toString(),
                rssi = beacon.rssi,
                distance = beacon.distance
            )

    }
}