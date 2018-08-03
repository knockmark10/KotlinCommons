package com.ia.mchaveza.kotlin_library

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.ia.mchaveza.kotlin_library.MapsStyles.*

/**
 * Choose between Aubergine, Dark, Night,
 * Retro, Silver or default style for
 *           your map
 */
fun GoogleMap.setMapStyle(mContext: Context, style: MapsStyles) {
    val rawStyle: Int =
            when (style) {
                Aubergine -> {
                    R.raw.map_auberine
                }
                Dark -> {
                    R.raw.map_dark
                }
                Night -> {
                    R.raw.map_night
                }
                Retro -> {
                    R.raw.map_retro
                }
                Silver -> {
                    R.raw.map_silver
                }
                Default -> {
                    R.raw.map_default
                }
            }
    try {
        val success = this.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        mContext, rawStyle))
        if (!success) {
            Log.e("Error", "Style parsing failed")
        }
    } catch (e: Resources.NotFoundException) {
        Log.e("Error", "Can't find style")
    }
}

/**
 * Sets the map style according to the daylight
 */
fun GoogleMap.setDaylightStyle(mContext: Context) {
    val style: MapsStyles =
            when {
                DateManager.getCurrentHour() in 8..11 -> {
                    Default
                }
                DateManager.getCurrentHour() in 12..18 -> {
                    Retro
                }
                DateManager.getCurrentHour() in 19..21 -> {
                    Aubergine
                }
                DateManager.getCurrentHour() in 22..23 -> {
                    Night
                }
                else -> {
                    Dark
                }
            }
    this.setMapStyle(mContext, style)
}

/**
 * Set the camera to desire position with custom zoom
 */
fun GoogleMap.setCurrentPosition(position: LatLng, zoom: Int) {
    val cameraPosition = CameraPosition.Builder().target(position).zoom(zoom.toFloat()).build()
    this.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
}

/**
 * Centers the camera with a given marker list
 */
fun GoogleMap.centerCamera(listMarker: List<Marker>, zoom: Float) {
    var latitude = 0.0
    var longitude = 0.0
    for (marker in listMarker) {
        latitude += marker.position.latitude
        longitude += marker.position.longitude
    }

    if (listMarker.isNotEmpty()) {
        latitude /= listMarker.size
        longitude /= listMarker.size
    }

    val cameraPosition = CameraPosition.Builder().target(LatLng(latitude, longitude)).zoom(zoom).build()
    this.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
}

fun GoogleMap.centerMarkers(context: Context, latLngList: MutableList<LatLng>) {
    val latLngBounds = LatLngBounds.Builder()

    latLngList.forEach {
        latLngBounds.include(it)
    }

    val bounds = latLngBounds.build()
    val width = context.resources.displayMetrics.widthPixels
    val height = context.resources.displayMetrics.heightPixels
    val padding = (width * 0.10).toInt()

    val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
    this.animateCamera(cameraUpdate)
}