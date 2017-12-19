package com.ia.mchaveza.kotlin_library

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by mchaveza on 19/12/2017.
 */
class GeocoderManager(context: Context) {

    private val mContext: Context
    private val geocoder: Geocoder
    private var addresses: MutableList<Address?>

    init {
        mContext = context
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
            address = addresses[0]?.getAddressLine(i)
            if (address != null) {
                addressList.add(address)
            }
        }
        return addressList
    }

    fun getCurrentCity(latitude: Double, longitude: Double, maxResults: Int): String {
        fillDataBook(latitude, longitude, maxResults)
        return if (addresses.get(0)?.locality != null) {
            addresses.get(0)?.locality.toString()
        } else {
            addresses.get(0)?.subAdminArea.toString()
        }
    }

    fun getCurrentState(latitude: Double, longitude: Double, maxResults: Int): String? {
        fillDataBook(latitude, longitude, maxResults)
        return addresses.get(0)?.adminArea
    }

    fun getCurrentCountry(latitude: Double, longitude: Double, maxResults: Int): String? {
        fillDataBook(latitude, longitude, maxResults)
        return addresses.get(0)?.countryName
    }

    fun getPostalCode(latitude: Double, longitude: Double, maxResults: Int): String? {
        fillDataBook(latitude, longitude, maxResults)
        return addresses.get(0)?.postalCode
    }

    fun knownName(latitude: Double, longitude: Double, maxResults: Int): String? {
        fillDataBook(latitude, longitude, maxResults)
        return addresses.get(0)?.featureName
    }

}