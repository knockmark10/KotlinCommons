package com.ia.mchaveza.kotlin_library

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MapStyleOptions

/**
 * Created by mchaveza on 02/01/2018.
 */
class MapsManager(private val mContext: Context) {

    private lateinit var mGoogleMap: GoogleMap

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

}