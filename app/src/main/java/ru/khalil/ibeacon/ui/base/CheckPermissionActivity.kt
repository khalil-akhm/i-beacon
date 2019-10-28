package ru.khalil.ibeacon.ui.base

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.altbeacon.beacon.BeaconManager
import ru.khalil.ibeacon.R

abstract class CheckPermissionActivity: AppCompatActivity() {
    private val TAG = "PermissionActivity"
    private val PERMISSION_REQUEST_FINE_LOCATION = 1
    private val PERMISSION_REQUEST_BACKGROUND_LOCATION = 2
    private val PERMISSION_REQUEST_COARSE_LOCATION = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
        verifyBluetooth()
    }

    private fun checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            checkPermission29()
        } else {
            checkPermission23()
        }
    }

    private fun checkPermission23() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check 
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.location_access))
                    .setMessage(getString(R.string.location_access_text))
                    .setPositiveButton(android.R.string.ok, null)
                    .setOnDismissListener{
                        requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_COARSE_LOCATION)
                    }
                    .show()
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.Q)
    private fun checkPermission29(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (this.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                        val builder = AlertDialog.Builder(this)
                            .setTitle(getString(R.string.location_access))
                            .setMessage(getString(R.string.location_access_back_text))
                            .setPositiveButton(android.R.string.ok, null)
                            .setOnDismissListener {
                                requestPermissions(
                                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                                    PERMISSION_REQUEST_BACKGROUND_LOCATION
                                )
                            }.show()
                    } else {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle(getString(R.string.functionality_limited))
                        builder.setMessage(getString(R.string.functionality_limited_text))
                        builder.setPositiveButton(android.R.string.ok, null)
                        builder.setOnDismissListener{ }
                        builder.show()
                    }

                }
            } else {
                if (this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        ),
                        PERMISSION_REQUEST_FINE_LOCATION
                    )
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Functionality limited")
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.  Please go to Settings -> Applications -> Permissions and grant location access to this app.")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setOnDismissListener { }
                    builder.show()
                }

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_FINE_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "fine location permission granted")
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Functionality limited")
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setOnDismissListener { }
                    builder.show()
                }
                return
            }
            PERMISSION_REQUEST_BACKGROUND_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "background location permission granted")
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Functionality limited")
                    builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons when in the background.")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setOnDismissListener { }
                    builder.show()
                }
                return
            }

            PERMISSION_REQUEST_COARSE_LOCATION ->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted")
                } else {
                    AlertDialog.Builder(this)
                        .setTitle(getString(R.string.functionality_limited))
                        .setMessage(getString(R.string.functionality_limited))
                        .setPositiveButton(android.R.string.ok, null)
                        .setOnDismissListener{}
                        .show()
                }
                return
            }
        }
    }

    private fun verifyBluetooth() {

        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                val builder = android.app.AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.bluetooth_not_enabled))
                builder.setMessage(getString(R.string.bluetooth_not_enabled_text))
                builder.setPositiveButton(android.R.string.ok, null)
                builder.setOnDismissListener {
                    //finish();
                    //System.exit(0);
                }
                builder.show()
            }
        } catch (e: RuntimeException) {
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.bluetooth_le_not_enabled))
            builder.setMessage(getString(R.string.bluetooth_le_not_enabled_text))
            builder.setPositiveButton(android.R.string.ok, null)
            builder.setOnDismissListener {
                //finish();
                //System.exit(0);
            }
            builder.show()

        }

    }
}