package ru.khalil.ibeacon.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.khalil.ibeacon.data.model.Beacon


@Database(entities = [
    Beacon::class
], version = 1)
abstract class BeaconDatabase: RoomDatabase() {

    abstract fun beacon(): BeaconDao

    companion object{
        @Volatile private var instance: BeaconDatabase? = null

        operator fun invoke(context: Context) = instance ?: synchronized(this){
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                    BeaconDatabase::class.java, "iBeacon.db")
                    .fallbackToDestructiveMigration()
                    .build()
    }
}