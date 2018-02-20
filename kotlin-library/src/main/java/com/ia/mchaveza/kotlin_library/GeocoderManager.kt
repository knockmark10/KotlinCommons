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
    private var geocoderMap: GeocoderResponse? = null

    fun getReverseGeocoder(latitude: Double, longitude: Double) {
        retrofit = Retrofit.Builder()
                .baseUrl(mContext.getString(R.string.geocoder_endpoint))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        interfaceAPI = retrofit?.create(InterfaceAPI::class.java)

        interfaceAPI?.getGeocoderData("json", latitude, longitude)?.enqueue(object : Callback<GeocoderResponse?> {
            override fun onResponse(call: Call<GeocoderResponse?>?, response: Response<GeocoderResponse?>?) {
                response?.body()?.let { item ->
                    geocoderMap = item
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

    fun getStreetName(): String {
        var street = "No disponible"
        geocoderMap?.let {
            if (it.result != null && it.result.isNotEmpty()) {
                street = it.result[0]?.streetName.toString()
            }
        }
        return street
    }

    fun getZipCode(): String {
        var zipCode = "No disponible"
        geocoderMap?.let {
            if (it.result != null && it.result.isNotEmpty()) {
                zipCode = it.result[0]?.zipCode.toString()
            }
        }
        return zipCode
    }


    fun getCity(): String {
        var city = "No disponible"
        geocoderMap?.let {
            if (it.result != null && it.result.isNotEmpty()) {
                city = it.result[0]?.city.toString()
            }
        }
        return city
    }

    fun getState(): String {
        var state = "No disponible"
        geocoderMap?.let {
            if (it.result != null && it.result.isNotEmpty()) {
                state = it.result[0]?.state.toString()
            }
        }
        return state
    }

    fun getCountryCode(): String {
        var countryCode = "No disponible"
        geocoderMap?.let {
            if (it.result != null && it.result.isNotEmpty()) {
                countryCode = it.result[0]?.countryCode.toString()
            }
        }
        return countryCode
    }

    fun getFormatedFull(): String {
        var formattedFull = "No disponible"
        geocoderMap?.let {
            if (it.result != null && it.result.isNotEmpty()) {
                formattedFull = it.result[0]?.formatedFull.toString()
            }
        }
        return formattedFull
    }

    fun getFormatedPostal(): String {
        var formattedPostal = "No disponible"
        geocoderMap?.let {
            if (it.result != null && it.result.isNotEmpty()) {
                formattedPostal = it.result[0]?.formatedPostal.toString()
            }
        }
        return formattedPostal
    }

    interface GeoCoderCallback {
        fun getData(response: GeocoderResponse)

        fun getDataError(error: String)
    }

}

