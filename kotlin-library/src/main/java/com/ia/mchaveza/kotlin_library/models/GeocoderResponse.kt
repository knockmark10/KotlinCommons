package com.ia.mchaveza.kotlin_library.models

import com.google.gson.annotations.SerializedName

data class GeocoderResponse(

        @field:SerializedName("result")
        val result: List<ResultItem?>? = null,

        @field:SerializedName("QTime")
        val qTime: Int? = null,

        @field:SerializedName("numFound")
        val numFound: Int? = null,

        @field:SerializedName("attributions")
        val attributions: String? = null
)