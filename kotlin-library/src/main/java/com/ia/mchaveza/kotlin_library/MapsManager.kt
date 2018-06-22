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

}

enum class MapsStyles {
    Aubergine,
    Dark,
    Night,
    Retro,
    Silver,
    Default
}