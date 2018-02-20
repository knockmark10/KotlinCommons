package com.ia.mchaveza.kotlin_library.interfaces

import com.ia.mchaveza.kotlin_library.models.GeocoderResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by mchaveza on 20/02/2018.
 */
interface InterfaceAPI {

    @GET("/reversegeocoding/search")
    fun getGeocoderData(
            @Query("format") format: String,
            @Query("lat") latitude: Double,
            @Query("lng") longitude: Double
    ): Call<GeocoderResponse?>
}