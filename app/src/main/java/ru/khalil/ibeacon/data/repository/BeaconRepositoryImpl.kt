package ru.khalil.ibeacon.data.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.khalil.ibeacon.data.database.BeaconDao
import ru.khalil.ibeacon.data.model.Beacon


class BeaconRepositoryImpl(
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