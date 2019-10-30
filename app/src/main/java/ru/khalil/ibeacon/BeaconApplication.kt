package ru.khalil.ibeacon

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import ru.khalil.ibeacon.data.database.BeaconDatabase
import ru.khalil.ibeacon.data.network.BeaconApiService
import ru.khalil.ibeacon.data.repository.BeaconRepository
import ru.khalil.ibeacon.data.repository.BeaconRepositoryImpl
import ru.khalil.ibeacon.ui.detail.BeaconDetailViewModelFactory
import ru.khalil.ibeacon.ui.list.MonitoringViewModelFactory

class BeaconApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@BeaconApplication))

        bind() from singleton { BeaconApiService() }
        bind() from singleton { BeaconDatabase(instance()) }
        bind() from singleton { instance<BeaconDatabase>().beacon() }

        bind<BeaconRepository>() with singleton { BeaconRepositoryImpl(instance(), instance()) }
        bind() from provider { MonitoringViewModelFactory(instance()) }
        bind() from provider { BeaconDetailViewModelFactory(instance()) }

    }

}