package com.ia.mchaveza.kotlin_library

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import com.tbruyelle.rxpermissions2.RxPermissions

@Suppress("unused")
class PermissionManager(private val mActivity: Activity, private val mListener: PermissionCallback? = null) {

    companion object {
        const val LOCATION_PERMISSION = 99
        const val ACCOUNT_PERMISSION = 101
        const val PHONE_STATE_PERMISSION = 291
        const val MULTIPLE_PERMISSIONS = 999
    }

    private val rxPermission = RxPermissions(mActivity)

    /**
     * This function allows you to request single permission
     * without splitting the process in two different places.
     * You only need to specify:
     * @param permission you want to request
     * @return boolean -> true if it needs to be requested
     *                    false ft it was already requested
     */
    fun requestSinglePermission(permission: String): Boolean =
            if (!permissionGranted(permission)) {
                rxPermission
                        .request(permission)
                        .subscribe({ granted ->
                            if (granted) {
                                mListener?.onPermissionGranted(permission)
                            } else {
                                mListener?.onPermissionDenied(permission)
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
                                mListener?.onPermissionGranted(permission.name)
                            }
                            permission.shouldShowRequestPermissionRationale -> {
                                mListener?.onPermissionDenied(permission.name)
                            }
                            else -> {
                                mListener?.onPermissionDenied(permission.name)
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

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value [PackageManager.PERMISSION_GRANTED].
     *
     * @see Activity.onRequestPermissionsResult
     */
    fun verifyPermissions(grantResults: IntArray): Boolean {
        // At least one result must be checked.
        if (grantResults.isEmpty()) {
            return false
        }

        // Verify that each required permission has been granted, otherwise return false.
        return grantResults.none { it != PackageManager.PERMISSION_GRANTED }
    }

    @Deprecated("Use either verifyPermissions or permissionGranted instead.")
    fun checkPermissions(permissionList: Permissions): Boolean {
        return when (permissionList) {
            Permissions.LOCATION -> {
                ActivityCompat.checkSelfPermission(mActivity, permissionList.type) == PackageManager.PERMISSION_GRANTED
            }
            Permissions.ACCOUNT -> {
                ActivityCompat.checkSelfPermission(mActivity, permissionList.type) == PackageManager.PERMISSION_GRANTED
            }
            Permissions.PHONE_STATE -> {
                ActivityCompat.checkSelfPermission(mActivity, permissionList.type) == PackageManager.PERMISSION_GRANTED
            }
            Permissions.MANY -> {
                ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    /**
     * Use this method when you want to request permissions at runtime.
     * When you only require one type of permission, you only need the
     * first parameter with the desired one. If you want to request
     * multiple permissions, you need both parameters
     * @param permission -> Type of permission
     * @param permissionList -> Array of permissions to request
     *
     * i.e. requestPermissions(Permissions.LOCATION) -> Request Location Permission
     * i.e. requestPermissions(Permissions.MANY, arrayOf(Permissions.LOCATION.type, Permissions.PHONE_STATE.type))
     */
    @Deprecated("This doesn't support many permissions. Please refer to requestMultiplePermissions instead.")
    fun requestPermissions(permission: Permissions, permissionList: Array<String>? = null) {
        when (permission) {
            Permissions.LOCATION -> {
                ActivityCompat.requestPermissions(mActivity, arrayOf(permission.type), LOCATION_PERMISSION)
            }
            Permissions.ACCOUNT -> {
                ActivityCompat.requestPermissions(mActivity, arrayOf(permission.type), ACCOUNT_PERMISSION)
            }
            Permissions.PHONE_STATE -> {
                ActivityCompat.requestPermissions(mActivity, arrayOf(permission.type), PHONE_STATE_PERMISSION)
            }
            Permissions.MANY -> {
                permissionList?.let {
                    ActivityCompat.requestPermissions(mActivity, it, MULTIPLE_PERMISSIONS)
                }
            }
        }
    }
}

interface PermissionCallback {
    fun onPermissionGranted(permission: String)
    fun onPermissionDenied(permission: String)
}

enum class Permissions(val type: String) {
    LOCATION(Manifest.permission.ACCESS_FINE_LOCATION),
    ACCOUNT(Manifest.permission.GET_ACCOUNTS),
    PHONE_STATE(Manifest.permission.READ_PHONE_STATE),
    MANY("Many")
}