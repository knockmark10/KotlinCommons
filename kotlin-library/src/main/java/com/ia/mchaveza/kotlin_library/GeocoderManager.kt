package com.ia.mchaveza.kotlin_library

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.io.IOException
import java.util.*

/**
 * Created by mchaveza on 19/12/2017.
 */
class GeocoderManager(private val mContext: Context) {

    @Throws(IOException::class)
    fun getPostalCodeByCoordinates(context: Context, lat: Double, lon: Double, errorMessage: String): String? {
        val geoCoder = Geocoder(context, Locale.getDefault())
        var zipCode = errorMessage
        var address: Address? = null

        val addresses = geoCoder.getFromLocation(lat, lon, 5)
        if (addresses != null && addresses.size > 0) {
            for (i in addresses.indices) {
                address = addresses[i]
                if (address?.postalCode != null) {
                    zipCode = address.postalCode
                    break
                }
            }
            return zipCode
        }
        return errorMessage
    }

}

