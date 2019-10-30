package ru.khalil.ibeacon.data.repository

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.khalil.ibeacon.data.database.BeaconDao
import ru.khalil.ibeacon.data.model.Beacon
import ru.khalil.ibeacon.data.network.BeaconApiService


class BeaconRepositoryImpl(
    private val apiService: BeaconApiService,
    private val beaconDao: BeaconDao
) : BeaconRepository {

    override fun saveBeacon(beacons: List<Beacon>) {
        GlobalScope.launch(Dispatchers.IO) {
            beacons.forEach {
                if (beaconDao.get(it.uuid, it.major, it.minor) == null){
                    beaconDao.insert(it)
                } else {
                    it.run {
                        beaconDao.update(uuid, major, minor, rssi, distance)
                    }
                }
            }
        }
        saveBeaconsInServer(beacons)
    }

    private fun saveBeaconsInServer(beacons: List<Beacon>){
        GlobalScope.launch {
            try {
                val gson = Gson()
                val beaconsJson = gson.toJson(beacons)
                apiService.storeBeaconsAsync(beaconsJson).await()
            } catch (e: Exception){
                e.printStackTrace()
            }

        }
    }

    override fun getBeacon(id: Long): LiveData<Beacon> {
        return beaconDao.getLive(id)
    }

    override fun getBeacons():LiveData<List<Beacon>>{
        return beaconDao.getAllLive()
    }

    override fun deleteAll() {
        GlobalScope.launch(Dispatchers.IO) {
            beaconDao.deleteAll()
        }
    }


}