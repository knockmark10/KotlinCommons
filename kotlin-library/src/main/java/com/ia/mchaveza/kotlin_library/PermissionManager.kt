package com.ia.mchaveza.kotlin_library

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat

class PermissionManager(private val mActivity: Activity) {

    companion object {
        const val LOCATION_PERMISSION = 99
        const val ACCOUNT_PERMISSION = 101
        const val PHONE_STATE_PERMISSION = 291
        const val MULTIPLE_PERMISSIONS = 999
    }

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

    fun checkPermissions(permissionList: Permissions): Boolean {
        return when (permissionList) {
            Permissions.LOCATION -> {
                ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            }
            Permissions.ACCOUNT -> {
                ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED
            }
            Permissions.PHONE_STATE -> {
                ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
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
    fun requestPermissions(permission: Permissions, permissionList: Array<String>? = null) {
        when (permission) {
            Permissions.LOCATION -> {
                ActivityCompat.requestPermissions(mActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION)
            }
            Permissions.ACCOUNT -> {
                ActivityCompat.requestPermissions(mActivity, arrayOf(Manifest.permission.GET_ACCOUNTS), ACCOUNT_PERMISSION)
            }
            Permissions.PHONE_STATE -> {
                ActivityCompat.requestPermissions(mActivity, arrayOf(Manifest.permission.READ_PHONE_STATE), PHONE_STATE_PERMISSION)
            }
            Permissions.MANY -> {
                permissionList?.let {
                    ActivityCompat.requestPermissions(mActivity, it, MULTIPLE_PERMISSIONS)
                }
            }
        }
    }

}

enum class Permissions(val type: String) {
    LOCATION(Manifest.permission.ACCESS_FINE_LOCATION),
    ACCOUNT(Manifest.permission.GET_ACCOUNTS),
    PHONE_STATE(Manifest.permission.READ_PHONE_STATE),
    MANY("Many")
}