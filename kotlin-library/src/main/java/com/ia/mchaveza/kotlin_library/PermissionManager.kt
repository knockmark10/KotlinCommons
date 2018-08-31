package com.ia.mchaveza.kotlin_library

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import com.tbruyelle.rxpermissions2.RxPermissions
import java.lang.NullPointerException

@Suppress("unused")
class PermissionManager(private val mActivity: Activity, private val mListener: PermissionCallback? = null) {

    private val rxPermission = RxPermissions(mActivity)

    /**
     * This function allows you to request single permission
     * without splitting the process in two different places.
     * You only need to specify:
     * @param permission must be of type [Manifest.permission]
     * @return boolean -> true if it needs to be requested
     *                    false ft it was already requested
     */
    fun requestSinglePermission(permission: String): Boolean {
        checkListener()
        return if (!permissionGranted(permission)) {
            rxPermission
                    .request(permission)
                    .subscribe { granted ->
                        if (granted) {
                            mListener?.onPermissionGranted(permission)
                        } else {
                            mListener?.onPermissionDenied(permission)
                        }
                    }
            true
        } else {
            false
        }
    }

    /**
     * This function allows you to request multiple permissions
     * without splitting the process in two different places.
     * You only need to specify:
     * @param permissions -> Every entry must be of type [Manifest.permission]
     * @return boolean -> permissions are valid or not
     */
    fun requestMultiplePermissions(vararg permissions: String): Boolean {
        checkListener()
        if (permissions.isNotEmpty()) {
            rxPermission
                    .requestEach(permissions.contentToString())
                    .subscribe { permission ->
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
                    }
            return true
        } else {
            return false
        }
    }

    /**
     * Check if some permission was request and granted
     * @param permission must be of type [Manifest.permission]
     */
    fun permissionGranted(permission: String): Boolean {
        return if (permission == Manifest.permission.GET_ACCOUNTS) {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 ||
                    ActivityCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED
        } else {
            ActivityCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Checks if some permission is declared in the manifest.
     * @param permission must be of type [Manifest.permission]
     */
    fun checkManifestPermission(permission: String): Boolean {
        var isPresent = false
        with(mActivity) {
            val packageInfo = packageManager.getPackageInfo(packageName, android.content.pm.PackageManager.GET_PERMISSIONS)
            val permissions = packageInfo.requestedPermissions
            permissions.forEach {
                if (it.toLowerCase().contains(permission.toLowerCase(), true)) {
                    isPresent = true
                }
            }
        }
        return isPresent
    }

    /**
     * Checks if multiple permissions were declared in the manifest.
     * Every entry in the given array must be of the value [Manifest.permission]
     * @return false if at least one permission is not present
     */
    private fun checkManifestPermissions(vararg permissions: String): Boolean {
        var isPresent = true
        permissions.forEach {
            isPresent = isPresent and checkManifestPermission(it)
        }
        return isPresent
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value [PackageManager.PERMISSION_GRANTED].
     *
     * @see Activity.onRequestPermissionsResult
     */
    fun verifyPermissions(grantResults: IntArray): Boolean {
        if (grantResults.isEmpty()) {
            return false
        }
        return grantResults.none { it != PackageManager.PERMISSION_GRANTED }
    }

    private fun checkListener() {
        if (mListener == null) {
            throw NullPointerException("PermissionCallback interface required. You need to pass it in constructor.")
        }
    }

}

interface PermissionCallback {
    fun onPermissionGranted(permission: String)
    fun onPermissionDenied(permission: String)
}