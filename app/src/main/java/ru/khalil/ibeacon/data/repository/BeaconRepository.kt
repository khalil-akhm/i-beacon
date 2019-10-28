package ru.khalil.ibeacon.data.repository

import androidx.lifecycle.LiveData
import ru.khalil.ibeacon.data.model.Beacon


interface BeaconRepository {
    fun saveBeacon(beacons: List<Beacon>)
    fun getBeacon(id: Long): LiveData<Beacon>
    fun getBeacons(): LiveData<List<Beacon>>
    fun deleteAll()
}