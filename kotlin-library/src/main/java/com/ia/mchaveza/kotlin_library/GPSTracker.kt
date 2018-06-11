package com.ia.mchaveza.kotlin_library

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import com.google.android.gms.maps.model.LatLng

/**
 * Created by mchaveza on 19/12/2017.
 */
@Deprecated("Use LocationServices from this library instead")
class GPSTracker(private val mContext: Context) : Service(), LocationListener {

    /**
     * INSTANCES OF CLASSES
     */
    private var location: Location? = null
    private var locationManager: LocationManager? = null
    private var isGPSEnabled = false
    private var isNetworkEnabled = false
    private var longitude: Double = 0.toDouble()
    private var latitude: Double = 0.toDouble()
    private var mListener: LocationHasChangedCallback? = null

    init {
        getLocation()
    }

    /**
     * This functions tries to get user's location
     * if it's available. Otherwise it handles errors
     * to avoid collapsing
     *
     * It starts when this class is instantiated
     */
    private fun getLocation(): Location? {
        try {
            locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            isGPSEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
            isNetworkEnabled = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false

            if (isGPSEnabled && isNetworkEnabled) {
                if (isNetworkEnabled) {
                    try {
                        locationManager?.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                    } catch (se: SecurityException) {
                    }
                }

                if (locationManager != null) {
                    try {
                        location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
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
                        locationManager?.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                    } catch (se: SecurityException) {
                    }

                    if (locationManager != null) {
                        try {
                            location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        } catch (se: SecurityException) {
                        }

                        latitude = location?.latitude ?: 0.0
                        longitude = location?.longitude ?: 0.0
                    }
                }
            }

        } catch (e: Exception) {
        }
        return location
    }

    /**
     * Stops receiving updates from location services
     */
    fun stopUsingGPS() {
        if (locationManager != null) {
            try {
                locationManager?.removeUpdates(this@GPSTracker)
                locationManager = null
            } catch (se: SecurityException) {
            }
        }
    }

    /**
     * Get the last known latitude from user's location
     * @return latitude
     */
    fun getLatitude(): Double {
        if (location != null) {
            latitude = location?.latitude ?: 0.0
        }
        return latitude
    }

    /**
     * Get the last known longitude from user's location
     * @return latitude
     */
    fun getLongitude(): Double {
        if (location != null) {
            longitude = location?.longitude ?: 0.0
        }
        return longitude
    }

    /**
     * It checks if location is available at a specific moment
     * @return true if available
     */
    fun canGetLocation(): Boolean =
            locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false ||
                    locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false

    /**
     * Start receiving updates from location service
     */
    fun startListener(listener: LocationHasChangedCallback) {
        mListener = listener
    }

    /**
     * Stop receiving updates from location services
     */
    fun stopListener() {
        mListener = null
    }

    /**
     * Once our service is up and running, we receive updates from
     * out location service.
     */
    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude

        if (locationManager != null && mListener != null) {
            mListener?.onLocationHasChanged(LatLng(latitude, longitude))
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
    }

    companion object {
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 1
        private const val MIN_TIME_BW_UPDATES = (1000 * 1).toLong()
    }
}
