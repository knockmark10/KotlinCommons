package com.ia.mchaveza.kotlin_library

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

class FingerPrintUtils(private val mActivity: Activity, private val mListener: FingerPrintCallback) : PermissionCallback, FingerprintHelper.FingerPrintHelper {

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val ORSAN_KEY = "FingerKey"
    }

    private lateinit var cipher: Cipher
    private lateinit var keyStore: KeyStore
    private lateinit var keyGenerator: KeyGenerator
    private lateinit var cryptoObject: FingerprintManager.CryptoObject
    private lateinit var fingerPrintManager: FingerprintManager
    private lateinit var keyGuardManager: KeyguardManager

    private val permissionManager by lazy { PermissionManager(mActivity, this) }

    /**
     * This method validates if your device support fingertip detection
     * and you have everything setup
     */
    @SuppressLint("InlinedApi", "MissingPermission")
    fun validateFingerPrint() {
        keyGuardManager = mActivity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        fingerPrintManager = mActivity.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
        if (checkFingerPrintSensor()) {
            if (permissionManager.permissionGranted(android.Manifest.permission.USE_FINGERPRINT)) {
                if (fingerPrintManager.hasEnrolledFingerprints()) {
                    if (keyGuardManager.isKeyguardSecure) {
                        mListener.onFingerPrintReady()
                    } else {
                        mListener.onLockScreenSecurityDisabled()
                    }
                } else {
                    mListener.onNoFingerPrintRegistered()
                }
            } else {
                permissionManager.requestSinglePermission(Manifest.permission.USE_FINGERPRINT)
            }
        } else {
            mListener.onNoFingerPrintSensor()
        }
    }

    /**
     * Call this method when you want to start scanning
     * your fingertip
     */
    @SuppressLint("NewApi")
    fun startAuthProcess() {
        generateKey()
        if (initCipher()) {
            cryptoObject = FingerprintManager.CryptoObject(cipher)
            val helper = FingerprintHelper(fingerPrintManager, this)
            helper.startAuth(cryptoObject)
        }
    }

    @SuppressLint("InlinedApi")
    private fun generateKey() {
        try {
            keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            keyStore.load(null)

            val keyProperties = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            val builder = KeyGenParameterSpec.Builder(ORSAN_KEY, keyProperties)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)

            keyGenerator.run {
                init(builder.build())
                generateKey()
            }
        } catch (e: Throwable) {
            mListener.onAuthProcessFailed(e)
        }
    }

    @SuppressLint("InlinedApi")
    private fun initCipher(): Boolean {
        return try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7)
            keyStore.load(null)
            val key = keyStore.getKey(ORSAN_KEY, null)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            true
        } catch (exception: Throwable) {
            mListener.onAuthProcessFailed(exception)
            false
        }
    }

    private fun checkFingerPrintSensor() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        mActivity.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    } else {
        false
    }

    override fun onPermissionDenied(permission: String) {
        mListener.onFingerPrintPermissionDenied()
    }

    override fun onPermissionGranted(permission: String) {
        validateFingerPrint()
    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
        mListener.onAuthenticationError(errorCode, errString)
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
        mListener.onAuthenticationHelp(helpCode, helpString)
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
        mListener.onAuthenticationSucceeded(result)
    }

    override fun onAuthenticationFailed() {
        mListener.onAuthenticationFailed()
    }

    override fun onTooManyAttempts(errorCode: Int, errString: CharSequence?) {
        mListener.onTooManyAttempts(errorCode, errString)
    }

    interface FingerPrintCallback {
        fun onNoFingerPrintSensor()
        fun onFingerPrintPermissionDenied()
        fun onNoFingerPrintRegistered()
        fun onLockScreenSecurityDisabled()
        fun onAuthProcessFailed(error: Throwable)
        fun onAuthenticationError(errorCode: Int, errString: CharSequence?)
        fun onAuthenticationFailed()
        fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?)
        fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?)
        fun onTooManyAttempts(errorCode: Int, errString: CharSequence?)
        fun onFingerPrintReady()
    }

}