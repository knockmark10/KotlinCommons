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
class GeocoderManager(private val mContext: Context) {

    private var retrofit: Retrofit? = null
    private var interfaceAPI: InterfaceAPI? = null
    private var geocoderMap: GeocoderResponse? = null
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
                    geocoderMap = item
                }
            }

            override fun onFailure(call: Call<GeocoderResponse?>?, t: Throwable?) {
                t?.message?.let { errorMessage ->
                    error = errorMessage
                }
            }
        })

    }

    fun getStreetName(): String {
        var street = "No disponible"
        geocoderMap?.let { response ->
            if (response.result != null && response.result.isNotEmpty()) {
                street = response.result[0]?.streetName.toString()
            }
        }
        return street
    }

    fun getZipCode(): String {
        var zipCode = "No disponible"
        geocoderMap?.let { response ->
            if (response.result != null && response.result.isNotEmpty()) {
                zipCode = response.result[0]?.zipCode.toString()
            }
        }
        return zipCode
    }

    fun getCity(): String {
        var city = "No disponible"
        geocoderMap?.let { response ->
            if (response.result != null && response.result.isNotEmpty()) {
                city = response.result[0]?.city.toString()
            }
        }
        return city
    }

    fun getState(): String {
        var state = "No disponible"
        geocoderMap?.let { response ->
            if (response.result != null && response.result.isNotEmpty()) {
                state = response.result[0]?.state.toString()
            }
        }
        return state
    }

    fun getCountryCode(): String {
        var countryCode = "No disponible"
        geocoderMap?.let { response ->
            if (response.result != null && response.result.isNotEmpty()) {
                countryCode = response.result[0]?.countryCode.toString()
            }
        }
        return countryCode
    }

    fun getFormatedFull(): String {
        var formatedFull = "No disponible"
        geocoderMap?.let { response ->
            if (response.result != null && response.result.isNotEmpty()) {
                formatedFull = response.result[0]?.formatedFull.toString()
            }
        }
        return formatedFull
    }

    fun getFormatedPostal(): String {
        var formatedPostal = "No disponible"
        geocoderMap?.let { response ->
            if (response.result != null && response.result.isNotEmpty()) {
                formatedPostal = response.result[0]?.formatedPostal.toString()
            }
        }
        return formatedPostal
    }

}

