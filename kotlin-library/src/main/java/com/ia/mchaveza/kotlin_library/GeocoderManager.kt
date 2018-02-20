package com.ia.mchaveza.kotlin_library

import android.content.Context
import com.ia.mchaveza.kotlin_library.interfaces.InterfaceAPI
import com.ia.mchaveza.kotlin_library.models.GeocoderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by mchaveza on 19/12/2017.
 */
class GeocoderManager(private val mContext: Context, private val geoCoderCallback: GeoCoderCallback) {

    private var retrofit: Retrofit? = null
    private var interfaceAPI: InterfaceAPI? = null
    private var error: String? = null

    fun getReverseGeocoder(latitude: Double, longitude: Double) {
        retrofit = Retrofit.Builder()
                .baseUrl(mContext.getString(R.string.geocoder_endpoint))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        interfaceAPI = retrofit?.create(InterfaceAPI::class.java)

        interfaceAPI?.getGeocoderData("json", latitude, longitude)?.enqueue(object : Callback<GeocoderResponse?> {
            override fun onResponse(call: Call<GeocoderResponse?>?, response: Response<GeocoderResponse?>?) {
                response?.body()?.let { item ->
                    geoCoderCallback.getData(item)
                }
            }

            override fun onFailure(call: Call<GeocoderResponse?>?, t: Throwable?) {
                t?.message?.let { errorMessage ->
                    geoCoderCallback.getDataError(errorMessage)
                    error = errorMessage
                }
            }
        })

    }

    fun getStreetName(geocoderMap: GeocoderResponse): String {
        var street = "No disponible"
        if (geocoderMap.result != null && geocoderMap.result.isNotEmpty()) {
            street = geocoderMap.result[0]?.streetName.toString()
        }
        return street
    }


    fun getZipCode(geocoderMap: GeocoderResponse): String {
        var zipCode = "No disponible"
        if (geocoderMap.result != null && geocoderMap.result.isNotEmpty()) {
            zipCode = geocoderMap.result[0]?.zipCode.toString()
        }
        return zipCode
    }


    fun getCity(geocoderMap: GeocoderResponse): String {
        var city = "No disponible"
        if (geocoderMap.result != null && geocoderMap.result.isNotEmpty()) {
            city = geocoderMap.result[0]?.city.toString()
        }
        return city
    }

    fun getState(geocoderMap: GeocoderResponse): String {
        var state = "No disponible"
        if (geocoderMap.result != null && geocoderMap.result.isNotEmpty()) {
            state = geocoderMap.result[0]?.state.toString()
        }
        return state
    }

    fun getCountryCode(geocoderMap: GeocoderResponse): String {
        var countryCode = "No disponible"
        if (geocoderMap.result != null && geocoderMap.result.isNotEmpty()) {
            countryCode = geocoderMap.result[0]?.countryCode.toString()
        }
        return countryCode
    }

    fun getFormatedFull(geocoderMap: GeocoderResponse): String {
        var formattedFull = "No disponible"
        if (geocoderMap.result != null && geocoderMap.result.isNotEmpty()) {
            formattedFull = geocoderMap.result[0]?.formatedFull.toString()
        }
        return formattedFull
    }

    fun getFormatedPostal(geocoderMap: GeocoderResponse): String {
        var formattedPostal = "No disponible"
        if (geocoderMap.result != null && geocoderMap.result.isNotEmpty()) {
            formattedPostal = geocoderMap.result[0]?.formatedPostal.toString()
        }
        return formattedPostal
    }

    interface GeoCoderCallback {
        fun getData(response: GeocoderResponse)

        fun getDataError(error: String)
    }

}

