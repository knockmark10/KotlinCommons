package com.ia.mchaveza.kotlin_library

import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.*
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import com.google.android.gms.maps.model.LatLng
import java.io.IOException

/**
 * Created by mchaveza on 19/12/2017.
 */

class GPSTracker(private val mContext: Context) : Service(), LocationListener {

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
    private var mListener: LocationHasChangedCallback? = null

    init {
        getLocation()
    }

    fun setListener(listener: LocationHasChangedCallback) {
        mListener = listener
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
                    } catch (se: SecurityException) {
                    }

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
                    } catch (se: SecurityException) {
                    }

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

        } catch (e: Exception) {
        }
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

    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude

        if (locationManager != null && mListener != null) {
            mListener?.onLocationHasChanged(LatLng(latitude, longitude))
            getPostalCode(location)
        }
    }

    private fun getPostalCode(location: Location) {
        val address = getAddress(location)
        var postalCode: String

        if (address != null) {
            postalCode = if (address.postalCode != null) {
                address.postalCode
            } else {
                val temporalString = address.getAddressLine(2)
                val postalCodeSplit = temporalString.split(" ")
                postalCodeSplit[0]
            }
            mListener?.onGetPostalCode(postalCode)
        } else {
            mListener?.onGetPostalCode("No disponible")
        }

    }

    private fun getAddress(location: Location?): Address? {
        if (location == null) {
            return null
        }

        val geocoder = Geocoder(mContext)
        val addresses: List<Address>?
        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return if (addresses != null && !addresses.isEmpty()) {
            addresses[0]
        } else {
            null
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

    interface LocationHasChangedCallback {
        fun onLocationHasChanged(newLocation: LatLng)
        fun onGetPostalCode(postalCode: String)
    }

    companion object {
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 1
        private const val MIN_TIME_BW_UPDATES = (1000 * 1).toLong()
    }
}
