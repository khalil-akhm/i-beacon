package ru.khalil.ibeacon.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.khalil.ibeacon.data.repository.BeaconRepository

class MonitoringViewModelFactory(
    private val repository: BeaconRepository
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MonitoringViewModel::class.java)) {
            return MonitoringViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}