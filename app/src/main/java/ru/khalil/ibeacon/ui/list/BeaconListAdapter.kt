package ru.khalil.ibeacon.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list.view.*
import ru.khalil.ibeacon.data.model.Beacon
import ru.khalil.ibeacon.R
import kotlin.math.pow
import kotlin.math.round

class BeaconListAdapter(
    var beacons: List<Beacon>,
    private val onItemClickListener: (Beacon) -> Unit
    ): RecyclerView.Adapter<BeaconViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeaconViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item = inflater.inflate(R.layout.item_list, parent, false)
        return BeaconViewHolder(item)
    }

    override fun getItemCount() = beacons.size

    override fun onBindViewHolder(holder: BeaconViewHolder, position: Int) {
        val beacon = beacons[position]
        holder.view.apply {

            textUUID.text = beacon.uuid
            textMajor.text = beacon.major
            textMinor.text = beacon.minor
            textRSSI.text = beacon.rssi.toString()
            textDistance.text = context.getString(R.string.distance_m,
                beacon.distance.round(-2).toString()
//                beacon.distance.r.roundToInt().toString()
            )


            setOnClickListener{
                onItemClickListener.invoke(beacon)
            }
        }
    }
}

fun Double.round(rank: Int): Double{
    val i = 1/10.0.pow(rank)
    return round(this * i)/i
}

class BeaconViewHolder(val view: View): RecyclerView.ViewHolder(view)
