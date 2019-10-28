package ru.khalil.ibeacon.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.khalil.ibeacon.data.model.Beacon
import ru.khalil.ibeacon.data.repository.BeaconRepository

class BeaconDetailViewModel(private val repository: BeaconRepository): ViewModel() {

    fun getBeacon(id : Long): LiveData<Beacon>{
        return repository.getBeacon(id)
    }
}