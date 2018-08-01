package com.ia.mchaveza.kotlin_library

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal

@TargetApi(Build.VERSION_CODES.M)
class FingerprintHelper(private val mManager: FingerprintManager, private val mListener: FingerPrintHelper) : FingerprintManager.AuthenticationCallback() {

    private var cancellationSignal: CancellationSignal? = null
    private var selfCancelled = false

    @SuppressLint("MissingPermission")
    fun startAuth(cryptoObject: FingerprintManager.CryptoObject) {
        startListening(cryptoObject)
    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
        if (!selfCancelled) {
            stopListening()
            if (errorCode == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
                mListener.onTooManyAttempts(errorCode, errString)
            } else {
                mListener.onAuthenticationError(errorCode, errString)
            }
        }
    }

    private fun startListening(cryptoObject: FingerprintManager.CryptoObject) {
        cancellationSignal = CancellationSignal()
        selfCancelled = false
        mManager.authenticate(cryptoObject, cancellationSignal, 0, this, null)
    }

    private fun stopListening() {
        cancellationSignal?.also {
            selfCancelled = true
            it.cancel()
        }
        cancellationSignal = null
    }

    override fun onAuthenticationFailed() {
        if (!selfCancelled) {
            stopListening()
        }
        mListener.onAuthenticationFailed()
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
        mListener.onAuthenticationHelp(helpCode, helpString)
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
        mListener.onAuthenticationSucceeded(result)
    }

    interface FingerPrintHelper {
        fun onAuthenticationError(errorCode: Int, errString: CharSequence?)
        fun onAuthenticationFailed()
        fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?)
        fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?)
        fun onTooManyAttempts(errorCode: Int, errString: CharSequence?)
    }

}