package com.ia.mchaveza.kotlin_library

import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import com.google.android.gms.maps.model.LatLng

/**
 * Created by mchaveza on 19/12/2017.
 */

class GPSTracker(private val mContext: Context, private var mListener: onLocationHasChanged?) : Service(), LocationListener {

    /**
     * INSTANCES OF CLASSES
     */
    private var location: Location? = null
    private var locationManager: LocationManager? = null
    private var isGPSEnabled = false
    private var isNetworkEnabled = false
    private var canGetLocation = false
    private var longitude: Double = 0.toDouble()
    private var latitude: Double = 0.toDouble()

    init {
        getLocation()
    }

    private fun getLocation(): Location? {
        try {
            locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true
                if (isNetworkEnabled) {
                    try {
                        locationManager!!.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                    } catch (se: SecurityException) {
                    }

                }

                if (locationManager != null) {
                    try {
                        location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    } catch (se: SecurityException) {}

                    if (location != null) {
                        latitude = location!!.latitude
                        longitude = location!!.longitude
                    }
                }
            }

            if (isGPSEnabled) {
                if (location == null) {
                    try {
                        locationManager!!.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                    } catch (se: SecurityException) {}

                    if (locationManager != null) {
                        try {
                            location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        } catch (se: SecurityException) {
                        }

                        if (location != null) {
                            latitude = location!!.latitude
                            longitude = location!!.longitude
                        }
                    }
                }
            }

        } catch (e: Exception) {}
        return location
    }

    fun stopUsingGPS() {
        if (locationManager != null) {
            try {
                locationManager!!.removeUpdates(this@GPSTracker)
                locationManager = null
            } catch (se: SecurityException) {
            }
        }
    }

    fun getLatitude(): Double {
        if (location != null) {
            latitude = location!!.latitude
        }
        return latitude
    }

    fun getLongitude(): Double {
        if (location != null) {
            longitude = location!!.longitude
        }
        return longitude
    }

    fun canGetLocation(): Boolean {
        return this.canGetLocation
    }

    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(mContext)
        alertDialog.setTitle("GPS")
        alertDialog.setMessage("GPS is not enabled. Would you like to go to Settings?")
        alertDialog.setPositiveButton("Settings") { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            mContext.startActivity(intent)
        }
        alertDialog.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        alertDialog.show()
    }

    fun unsubscribeLocationListener() {
        mListener = null
    }

    fun subscribeLocationListener(listener: onLocationHasChanged) {
        mListener = listener
    }

    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude

        if (locationManager != null && mListener != null) {
            mListener?.locationHasChanged(LatLng(latitude, longitude))
        }
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    interface onLocationHasChanged {
        fun locationHasChanged(newLocation: LatLng)
    }

    companion object {
        private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 1
        private val MIN_TIME_BW_UPDATES = (1000 * 1).toLong()
    }
}
