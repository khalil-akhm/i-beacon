package ru.khalil.ibeacon.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_beacon_deatil.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import ru.khalil.ibeacon.R
import ru.khalil.ibeacon.data.model.Beacon
import ru.khalil.ibeacon.ui.list.round


class BeaconDetailActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()

    private val viewModelFactory: BeaconDetailViewModelFactory by instance()
    private lateinit var viewModel: BeaconDetailViewModel


    companion object{
        private const val BEACON_KEY_ID = "beacon_key_id"

        fun getIntent(context: Context, beacon: Beacon): Intent{
            return Intent(context, BeaconDetailActivity::class.java).apply {
                putExtra(BEACON_KEY_ID, beacon.id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beacon_deatil)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getLongExtra(BEACON_KEY_ID, 0)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(BeaconDetailViewModel::class.java)

        viewModel.apply {
            if (id != 0L) {
                getBeacon(id).observe(
                    this@BeaconDetailActivity,
                    Observer { beacon ->
                        beacon ?: return@Observer
                        updateUI(beacon)
                    })
            }
        }
    }

    private fun updateUI(beacon: Beacon){
        textUUID.text = beacon.uuid
        textMajor.text = beacon.major
        textMinor.text = beacon.minor
        textRSSI.text = beacon.rssi.toString()
        textDistance.text = getString(R.string.distance_m, beacon.distance.round(-2).toString())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                this.finish()
            }
        }
        return true
    }
}
