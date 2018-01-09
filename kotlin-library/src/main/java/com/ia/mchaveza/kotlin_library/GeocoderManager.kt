package com.ia.mchaveza.kotlin_library

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by mchaveza on 19/12/2017.
 */
class GeocoderManager(private val mContext: Context) {

    private val geocoder: Geocoder
    private var addresses: MutableList<Address?>
    private var unavailable = "No disponible"

    init {
        geocoder = Geocoder(mContext, Locale.getDefault())
        addresses = ArrayList()
    }

    private fun fillDataBook(latitude: Double, longitude: Double, maxResults: Int) {
        addresses.clear()
        addresses = geocoder.getFromLocation(latitude, longitude, maxResults)
    }

    fun getCurrentAddress(latitude: Double, longitude: Double, maxResults: Int): MutableList<String> {
        var addressList: MutableList<String> = ArrayList()
        var address: String?
        fillDataBook(latitude, longitude, maxResults)
        for (i in 0..5) {
            if (addresses.size > 0) {
                address = addresses[0]?.getAddressLine(i)
                if (address != null) {
                    addressList.add(address)
                }
            }
        }
        return addressList
    }

    fun getCurrentCity(latitude: Double, longitude: Double, maxResults: Int): String {
        fillDataBook(latitude, longitude, maxResults)
        return if (addresses.size > 0 && addresses[0]?.locality != null) {
            addresses[0]?.locality.toString()
        } else {
            addresses[0]?.subAdminArea.toString()
        }
    }

    fun getCurrentState(latitude: Double, longitude: Double, maxResults: Int): String? {
        fillDataBook(latitude, longitude, maxResults)
        return if (addresses.size > 0 && addresses[0]?.adminArea != null) {
            addresses[0]?.adminArea
        } else {
            unavailable
        }
    }

    fun getCurrentCountry(latitude: Double, longitude: Double, maxResults: Int): String? {
        fillDataBook(latitude, longitude, maxResults)
        return if (addresses.size > 0 && addresses[0]?.countryName != null) {
            addresses[0]?.countryName
        } else {
            unavailable
        }
    }

    fun getPostalCode(latitude: Double, longitude: Double, maxResults: Int): String? {
        fillDataBook(latitude, longitude, maxResults)
        return if (addresses.size > 0 && addresses[0]?.postalCode != null) {
            addresses.get(0)?.postalCode
        } else {
            unavailable
        }
    }

    fun knownName(latitude: Double, longitude: Double, maxResults: Int): String? {
        fillDataBook(latitude, longitude, maxResults)
        return if (addresses.size > 0 && addresses[0]?.featureName != null) {
            addresses[0]?.featureName
        } else {
            unavailable
        }
    }

}