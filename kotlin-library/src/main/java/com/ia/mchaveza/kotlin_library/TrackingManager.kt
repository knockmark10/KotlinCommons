package com.ia.mchaveza.kotlin_library

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient


class TrackingManager(private val mContext: Context) : LocationCallback() {

    private val mLocationRequest by lazy { LocationRequest() }
    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0
    private var mFusedClient: FusedLocationProviderClient? = null

    private var mListener: TrackingManagerLocationCallback? = null

    companion object {
        const val UPDATE_INTERVAL = (15 * 1000).toLong()
        const val FASTEST_INTERVAL = (10 * 1000).toLong()
    }

    @Suppress("MissingPermission")
    fun startLocationUpdates(listener: TrackingManagerLocationCallback, updateInterval: Long? = null, fastestInterval: Long? = null, useLooper: Boolean = true) {
        val looper: Looper? = if (useLooper) {
            Looper.myLooper()
        } else {
            null
        }

        mListener = listener
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = updateInterval ?: UPDATE_INTERVAL
        mLocationRequest.fastestInterval = fastestInterval ?: FASTEST_INTERVAL

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(mContext)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        if (checkLocationPermissions()) {
            mFusedClient = getFusedLocationProviderClient(mContext)
            mFusedClient?.requestLocationUpdates(
                    mLocationRequest,
                    this,
                    looper)
        } else {
            mListener?.onLocationHasChangedError(Exception("Location permission was denied"))
        }
    }

    fun stopLocationUpdates() {
        mFusedClient?.removeLocationUpdates(this)
    }

    @Suppress("MissingPermission")
    fun getLastLocation() {
        if (checkLocationPermissions()) {
            val locationClient = getFusedLocationProviderClient(mContext)
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
            mListener?.onLocationHasChangedError(Exception("Location permission was denied"))
        }
    }

    fun getLatitude(): Double = this.userLatitude

    fun getLongitude(): Double = this.userLongitude

    fun areLocationServicesEnabled(): Boolean {
        val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        val isGpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
        val isNetworkAvailable = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                ?: false
        val isPermissionGranted = checkLocationPermissions()
        return isGpsEnabled && isNetworkAvailable && isPermissionGranted
    }

    private fun checkLocationPermissions(): Boolean =
            ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    override fun onLocationResult(locationResult: LocationResult) {
        super.onLocationResult(locationResult)
        userLatitude = locationResult.lastLocation.latitude
        userLongitude = locationResult.lastLocation.longitude
        mListener?.onLocationHasChanged(locationResult.lastLocation)
    }

}

interface TrackingManagerLocationCallback {
    fun onLocationHasChanged(location: Location)
    fun onLocationHasChangedError(error: Exception)
}