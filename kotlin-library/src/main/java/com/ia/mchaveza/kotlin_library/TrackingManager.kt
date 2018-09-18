package com.ia.mchaveza.kotlin_library

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient


class TrackingManager(private val mContext: Context) : LocationCallback(), PermissionCallback {

    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0
    private var permissionManager: PermissionManager? = null
    private var mFusedClient: FusedLocationProviderClient? = null
    private val mLocationRequest by lazy { LocationRequest() }

    private var mListener: TrackingManagerLocationCallback? = null

    companion object {
        const val UPDATE_INTERVAL = (15 * 1000).toLong()
        const val FASTEST_INTERVAL = (10 * 1000).toLong()
    }

    /**
     * Enables automatic runtime permission requests when permissions aren't granted.
     * This will check is permission is added in manifest and if it was rejected or
     * not. It is recommended that you call it for a better performance.
     */
    fun enablePermissionSetup(mAcivity: Activity) {
        permissionManager = PermissionManager(mAcivity, this)
    }

    @Suppress("MissingPermission")
    fun startLocationUpdates(listener: TrackingManagerLocationCallback, updateInterval: Long? = null, fastestInterval: Long? = null, useLooper: Boolean = true) {
        handleManifestPermissions()

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
            onPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
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
            onPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
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

    private fun handleManifestPermissions() {
        permissionManager?.let {
            if (!it.checkManifestPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                throw SecurityException("Manifest permission missing. You need ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION to use this feature.")
            }
            if (!it.permissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                it.requestSinglePermission(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    override fun onPermissionGranted(permission: String) {
        startLocationUpdates(mListener!!)
    }

    override fun onPermissionDenied(permission: String) {
        mListener?.onLocationHasChangedError(RuntimeException("Location services won't work if location permissions are denied. $permission is required."))
    }

}

interface TrackingManagerLocationCallback {
    fun onLocationHasChanged(location: Location)
    fun onLocationHasChangedError(error: Exception)
}