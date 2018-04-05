package com.ia.mchaveza.kotlin_library

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker

/**
 * Choose between Aubergine, Dark, Night,
 * Retro, Silver or default style for
 *           your map
 */
fun GoogleMap.setMapStyle(mContext: Context, style: MapsManager.Style) {
    var success: Boolean
    try {
        when (style.style) {
            MapsManager.Style.Aubergine.style -> {
                success = this.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                mContext, R.raw.map_auberine))
                if (!success) {
                    Log.e("Error", "Style parsing failed")
                }
            }
            MapsManager.Style.Dark.style -> {
                success = this.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                mContext, R.raw.map_dark))
                if (!success) {
                    Log.e("Error", "Style parsing failed")
                }
            }
            MapsManager.Style.Night.style -> {
                success = this.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                mContext, R.raw.map_night))
                if (!success) {
                    Log.e("Error", "Style parsing failed")
                }
            }
            MapsManager.Style.Retro.style -> {
                success = this.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                mContext, R.raw.map_retro))
                if (!success) {
                    Log.e("Error", "Style parsing failed")
                }
            }
            MapsManager.Style.Silver.style -> {
                success = this.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                mContext, R.raw.map_silver))
                if (!success) {
                    Log.e("Error", "Style parsing failed")
                }
            }
            MapsManager.Style.Default.style -> {
                success = this.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                mContext, R.raw.map_default))
                if (!success) {
                    Log.e("Error", "Style parsing failed")
                }
            }
        }
    } catch (e: Resources.NotFoundException) {
        Log.e("Error", "Can't find style")
    }
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