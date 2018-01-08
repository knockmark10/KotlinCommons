package com.ia.mchaveza.kotlin_library

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import java.io.File

/**
 * Created by mchaveza on 22/12/2017.
 */
class ImageUtils(private val mContext: Context) {

    fun changeDrawableColor(drawable: Drawable, color: Int): Drawable {
        val wrappedDrawable: Drawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(mContext.applicationContext, color))
        DrawableCompat.setTintMode(wrappedDrawable, PorterDuff.Mode.SRC_IN)
        return wrappedDrawable
    }

    fun loadGif(splashView: ImageView, @DrawableRes resource: Int){
        val imageViewTarget = GlideDrawableImageViewTarget(splashView)
        Glide.with(mContext)
                .load(resource)
                .into(imageViewTarget)
    }

    fun loadGifByUrl(splashView: ImageView, url: String){
        val imageViewTarget = GlideDrawableImageViewTarget(splashView)
        Glide.with(mContext)
                .load(url)
                .into(imageViewTarget)
    }

}