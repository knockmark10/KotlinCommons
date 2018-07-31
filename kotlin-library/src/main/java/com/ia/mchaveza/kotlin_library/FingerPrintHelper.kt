package com.ia.mchaveza.kotlin_library

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal

@TargetApi(Build.VERSION_CODES.M)
class FingerprintHelper(private val mListener: FingerPrintHelper) : FingerprintManager.AuthenticationCallback() {

    private lateinit var cancellationSignal: CancellationSignal

    @SuppressLint("MissingPermission")
    fun startAuth(manager: FingerprintManager, cryptoObject: FingerprintManager.CryptoObject) {
        cancellationSignal = CancellationSignal()
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null)
    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
        mListener.onAuthenticationError(errorCode, errString)
    }

    override fun onAuthenticationFailed() {
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
    }

}