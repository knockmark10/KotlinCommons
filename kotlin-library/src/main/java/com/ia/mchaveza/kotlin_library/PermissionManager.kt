package com.ia.mchaveza.kotlin_library

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import com.tbruyelle.rxpermissions2.RxPermissions

class PermissionManager(private val mActivity: Activity, private val mListener: PermissionCallback) {

    private val rxPermission = RxPermissions(mActivity)

    /**
     * This function allows you to request single permission
     * without splitting the process in two different places.
     * You only need to specify:
     * @param permission you want to request
     * @return boolean -> true if it needs to be requested
     *                    false ft it was already requested
     */
    fun requestLocationPermission(permission: String): Boolean =
            if (!permissionGranted(permission)) {
                rxPermission
                        .request(permission)
                        .subscribe({ granted ->
                            if (granted) {
                                mListener.onPermissionGranted(permission)
                            } else {
                                mListener.onPermissionDenied(permission)
                            }
                        })
                true
            } else {
                false
            }

    /**
     * This function allows you to request multiple permissions
     * without splitting the process in two different places.
     * You only need to specify:
     * @param permissions you want to request
     * @return boolean -> permissions are valid or not
     */
    fun requestMultiplePermissions(vararg permissions: String): Boolean {
        if (permissions.isNotEmpty()) {
            rxPermission
                    .requestEach(permissions.contentToString())
                    .subscribe({ permission ->
                        when {
                            permission.granted -> {
                                mListener.onPermissionGranted(permission.name)
                            }
                            permission.shouldShowRequestPermissionRationale -> {
                                mListener.onPermissionDenied(permission.name)
                            }
                            else -> {
                                mListener.onPermissionDenied(permission.name)
                            }
                        }
                    })
            return true
        } else {
            return false
        }
    }

    /**
     * Check if some permission was request and granted
     */
    fun permissionGranted(permission: String): Boolean =
            ActivityCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED
}

interface PermissionCallback {
    fun onPermissionGranted(permission: String)
    fun onPermissionDenied(permission: String)
}