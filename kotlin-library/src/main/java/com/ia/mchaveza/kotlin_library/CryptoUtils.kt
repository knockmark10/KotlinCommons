package com.ia.mchaveza.kotlin_library

import se.simbio.encryption.Encryption

object CryptoUtils {

    private const val salt = "ka3F1DpS"
    private val iv = byteArrayOf(-89, -19, 17, -83, 86, 106, -31, 30, -5, -111, 61, -75, -84, 95, 120, -53)
    private const val key = "1Hbfh667adfDEJ78"

    fun getInstance(): Encryption =
            Encryption.getDefault(key, salt, iv)

}

interface CryptoUtilsCallback {
    fun onSuccess(value: String)
    fun onError(error: Throwable)
}