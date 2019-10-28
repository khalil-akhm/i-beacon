package ru.khalil.ibeacon.ui.list

import androidx.lifecycle.ViewModel
import ru.khalil.ibeacon.data.model.Beacon
import ru.khalil.ibeacon.data.repository.BeaconRepository

class MonitoringViewModel(private val repository: BeaconRepository): ViewModel() {

    fun saveBeacons(beacons: List<Beacon>){
        repository.saveBeacon(beacons)
    }

    fun getBeacons() = repository.getBeacons()

    fun deleteAll() {
        repository.deleteAll()
    }

}