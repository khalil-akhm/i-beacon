package ru.khalil.ibeacon.ui.list

import android.os.Bundle
import android.os.RemoteException
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_monitoring.*
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Region
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import ru.khalil.ibeacon.R
import ru.khalil.ibeacon.data.model.Beacon
import ru.khalil.ibeacon.ui.base.CheckPermissionActivity
import ru.khalil.ibeacon.ui.detail.BeaconDetailActivity

class MonitoringActivity : CheckPermissionActivity(), BeaconConsumer, KodeinAware {
    override val kodein by closestKodein()
    private val TAG = "beaconsmonitoring"
    private val viewModelFactory: MonitoringViewModelFactory by instance()
    private lateinit var beaconManager: BeaconManager
    private lateinit var viewModel: MonitoringViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitoring)

        viewModel = ViewModelProviders.of(this,viewModelFactory).get(MonitoringViewModel::class.java)

        setBeaconManager()

        viewModel.getBeacons().observe(this, Observer {beacons ->
            beacons?: return@Observer
            initRecyclerView(beacons)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        beaconManager.unbind(this)
    }

    private fun setBeaconManager(){
        beaconManager = BeaconManager.getInstanceForApplication(this)
//        beaconManager.foregroundScanPeriod = 100
//        beaconManager.foregroundBetweenScanPeriod = 10
//        beaconManager.backgroundScanPeriod = 5100
//        beaconManager.backgroundBetweenScanPeriod = 2000
        beaconManager.beaconParsers.add(BeaconParser().
            setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
        beaconManager.bind(this)
    }


    override fun onBeaconServiceConnect() {
        try {
            beaconManager.apply {
                removeAllRangeNotifiers()
                addRangeNotifier { beaconsAlt, _ ->
                    val beacons = beaconsAlt.map { Beacon(it) }.sortedBy { it.distance }
//                    initRecyclerView(beacons)
                    viewModel.saveBeacons(beacons)
                }
                startRangingBeaconsInRegion(Region("myRangingUniqueId", null, null, null))

            }

        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    private fun initRecyclerView(beacons: List<Beacon>){
        recyclerView.apply{
            layoutManager = LinearLayoutManager(this@MonitoringActivity)
            if (adapter == null){
                adapter = BeaconListAdapter(beacons){ beacon ->
                    val intent = BeaconDetailActivity.getIntent(this@MonitoringActivity, beacon)
                    startActivity(intent)
                }
            } else {
                (adapter as BeaconListAdapter).apply {
                    this.beacons = beacons
                    notifyDataSetChanged()
                }
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.delete_all ->{
                viewModel.deleteAll()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
