package ru.khalil.ibeacon.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.khalil.ibeacon.data.model.Beacon

@Dao
interface BeaconDao {

    @Insert
    fun insert(beacon: Beacon)

    @Query("UPDATE  beacons SET rssi = :rssi, distance = :distance WHERE uuid = :uuid AND major = :major AND minor = :minor")
    fun update(uuid: String, major: String, minor: String, rssi: Int, distance: Double )

    @Delete
    fun delete(beacon: Beacon)

    @Query("SELECT * FROM beacons ORDER BY distance")
    fun getAllLive(): LiveData<List<Beacon>>

    @Query("SELECT * FROM beacons WHERE id = :id")
    fun getLive(id: Long): LiveData<Beacon>

    @Query("SELECT * FROM beacons WHERE uuid = :uuid AND major = :major AND minor = :minor")
    fun get(uuid: String, major: String, minor: String): Beacon?

    @Query("DELETE FROM beacons ")
    fun deleteAll()

}