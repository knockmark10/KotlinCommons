package com.ia.mchaveza.kotlin_library

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.annotation.DrawableRes
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
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
    fun setGoogleMap(googleMap: GoogleMap) {
        mGoogleMap = googleMap
    }

    /**
     * Choose between Aubergine, Dark, Night,
     * Retro, Silver or default style for
     *           your map
     */
    fun changeMapStyle(style: Style): GoogleMap {
        var success: Boolean
        try {
            when (style.style) {
                Style.Aubergine.style -> {
                    success = mGoogleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    mContext, R.raw.map_auberine))
                    if (!success) {
                        Log.e("Error", "Style parsing failed")
                    }
                }
                Style.Dark.style -> {
                    success = mGoogleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    mContext, R.raw.map_dark))
                    if (!success) {
                        Log.e("Error", "Style parsing failed")
                    }
                }
                Style.Night.style -> {
                    success = mGoogleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    mContext, R.raw.map_night))
                    if (!success) {
                        Log.e("Error", "Style parsing failed")
                    }
                }
                Style.Retro.style -> {
                    success = mGoogleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    mContext, R.raw.map_retro))
                    if (!success) {
                        Log.e("Error", "Style parsing failed")
                    }
                }
                Style.Silver.style -> {
                    success = mGoogleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    mContext, R.raw.map_silver))
                    if (!success) {
                        Log.e("Error", "Style parsing failed")
                    }
                }
                Style.Default.style -> {
                    success = mGoogleMap.setMapStyle(
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

    fun getMarkerBitmapFromView(@DrawableRes resourceId: Int): Bitmap {
        val customMarkerView = (mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.custom_marker, null)
        val markerImageView = customMarkerView.findViewById(R.id.custom_marker_icon) as ImageView

        markerImageView.setImageResource(resourceId)
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(0, 0, customMarkerView.measuredWidth, customMarkerView.measuredHeight)
        customMarkerView.buildDrawingCache()

        val returnedBitmap = Bitmap.createBitmap(customMarkerView.measuredWidth, customMarkerView.measuredHeight,
                Bitmap.Config.ARGB_8888)

        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)

        customMarkerView.background?.draw(canvas)

        customMarkerView.draw(canvas)
        return returnedBitmap
    }

    enum class Style(val style: String) {
        Aubergine("Aubergine"),
        Dark("Dark"),
        Night("Night"),
        Retro("Retro"),
        Silver("Silver"),
        Default("Default")
    }

}