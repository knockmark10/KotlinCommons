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
 * Created by mchaveza on 02/01/2018.
 */
class MapsManager(private val mContext: Context) {

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var cameraPosition: CameraPosition

    /**
     * Before using this class, you should
     * initialize your map to be changed
     */
    fun setGoogleMap(googleMap: GoogleMap){
        mGoogleMap = googleMap
    }

    /**
     * Choose between Aubergine, Dark, Night,
     * Retro, Silver or default style for
     *           your map
     */
    fun changeMapStyle(style: String): GoogleMap {
        var success: Boolean
        try {
            when (style) {
                "Aubergine" -> {
                    success = mGoogleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    mContext, R.raw.map_auberine))
                    if (!success) {
                        Log.e("Error", "Style parsing failed")
                    }
                }
                "Dark" -> {
                    success = mGoogleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    mContext, R.raw.map_dark))
                    if (!success) {
                        Log.e("Error", "Style parsing failed")
                    }
                }
                "Night" -> {
                    success = mGoogleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    mContext, R.raw.map_night))
                    if (!success) {
                        Log.e("Error", "Style parsing failed")
                    }
                }
                "Retro" -> {
                    success = mGoogleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    mContext, R.raw.map_retro))
                    if (!success) {
                        Log.e("Error", "Style parsing failed")
                    }
                }
                "Silver" -> {
                    success = mGoogleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    mContext, R.raw.map_silver));
                    if (!success) {
                        Log.e("Error", "Style parsing failed");
                    }
                }
                "Default" -> {
                    success = mGoogleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    mContext, R.raw.map_default));
                    if (!success) {
                        Log.e("Error", "Style parsing failed");
                    }
                }
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("Error", "Can't find style");
        }

        return mGoogleMap
    }

    /**
     * Set the camera to desire position with custom zoom
     */
    fun setCurrentPosition(position: LatLng, zoom: Int): GoogleMap {
        cameraPosition = CameraPosition.Builder().target(position).zoom(zoom.toFloat()).build()
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        return mGoogleMap
    }

    /**
     * Centers the camera with a given marker list
     */
    fun centerCamera(listMarker: List<Marker>): GoogleMap {
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

        cameraPosition = CameraPosition.Builder().target(LatLng(latitude, longitude)).zoom(11f).build()
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        return mGoogleMap
    }

}