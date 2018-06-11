package com.ia.mchaveza.kotlin_library

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient


class TrackingManager(private val mActivity: Context) {

    private val mLocationRequest by lazy { LocationRequest() }
    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0

    private var mListener: LocationHasChangedCallback? = null

    companion object {
        const val UPDATE_INTERVAL = (15 * 1000).toLong()
        const val FASTEST_INTERVAL = (10 * 1000).toLong()
        const val PERMISSIONS_REQUEST_LOCATION = 99
    }

    fun startLocationUpdates(listener: LocationHasChangedCallback, updateInterval: Long? = null, fastestInterval: Long? = null) {
        //Set the listener to start receiving updates
        mListener = listener

        // Create the location request to start receiving updates
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = updateInterval ?: UPDATE_INTERVAL
        mLocationRequest.fastestInterval = fastestInterval ?: FASTEST_INTERVAL

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()

        // Check whether location settings are satisfied
        val settingsClient = LocationServices.getSettingsClient(mActivity)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getFusedLocationProviderClient(mActivity).requestLocationUpdates(
                    mLocationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            userLatitude = locationResult.lastLocation.latitude
                            userLongitude = locationResult.lastLocation.longitude
                            mListener?.onLocationHasChanged(locationResult.lastLocation)
                        }
                    },
                    Looper.myLooper())
        }
    }

    fun getLastLocation() {
        if (checkLocationPermissions()) {
            val locationClient = getFusedLocationProviderClient(mActivity)
            locationClient.lastLocation
                    .addOnSuccessListener {
                        mListener?.onLocationHasChanged(it)
                        userLatitude = it.latitude
                        userLongitude = it.longitude
                    }
                    .addOnFailureListener {
                        mListener?.onLocationHasChangedError(it)
                    }
        } else {
            requestPermissions()
        }
    }

    fun getLatitude(): Double = this.userLatitude

    fun getLongitude(): Double = this.userLongitude

    private fun checkLocationPermissions(): Boolean =
            ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
                mActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_LOCATION
        )
    }

}

interface LocationHasChangedCallback {
    fun onLocationHasChanged(location: Location)
    fun onLocationHasChangedError(error: Exception)
}