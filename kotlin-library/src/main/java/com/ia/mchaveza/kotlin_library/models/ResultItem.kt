package com.ia.mchaveza.kotlin_library.models

import com.google.gson.annotations.SerializedName

data class ResultItem(

        @field:SerializedName("sourceId")
        val sourceId: Int? = null,

        @field:SerializedName("zipCode")
        val zipCode: String? = null,

        @field:SerializedName("azimuthEnd")
        val azimuthEnd: Int? = null,

        @field:SerializedName("lng")
        val lng: Double? = null,

        @field:SerializedName("distance")
        val distance: Double? = null,

        @field:SerializedName("city")
        val city: String? = null,

        @field:SerializedName("houseNumber")
        val houseNumber: String? = null,

        @field:SerializedName("length")
        val length: Double? = null,

        @field:SerializedName("geocodingLevel")
        val geocodingLevel: String? = null,

        @field:SerializedName("adm1Name")
        val adm1Name: String? = null,

        @field:SerializedName("formatedFull")
        val formatedFull: String? = null,

        @field:SerializedName("streetName")
        val streetName: String? = null,

        @field:SerializedName("adm2Name")
        val adm2Name: String? = null,

        @field:SerializedName("countryCode")
        val countryCode: String? = null,

        @field:SerializedName("formatedPostal")
        val formatedPostal: String? = null,

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("state")
        val state: String? = null,

        @field:SerializedName("azimuthStart")
        val azimuthStart: Int? = null,

        @field:SerializedName("lat")
        val lat: Double? = null
)