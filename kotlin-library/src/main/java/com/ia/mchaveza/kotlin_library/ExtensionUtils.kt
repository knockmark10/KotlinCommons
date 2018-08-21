package com.ia.mchaveza.kotlin_library

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import se.simbio.encryption.Encryption
import java.lang.Exception
import java.util.*

/**
 * Created by mchaveza on 22/12/2017.
 */
fun Drawable.changeDrawableColor(context: Context, color: Int): Drawable {
    val wrappedDrawable = DrawableCompat.wrap(this)
    DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, color))
    DrawableCompat.setTintMode(wrappedDrawable, PorterDuff.Mode.SRC_IN)
    return wrappedDrawable
}

fun FragmentManager.replaceFragment(containerViewId: Int, fragment: Fragment) {
    this
            .beginTransaction()
            .replace(containerViewId, fragment)
            .commit()
}

fun FragmentManager.replaceFragmentAllowingStateLoss(containerViewId: Int, fragment: Fragment) {
    this
            .beginTransaction()
            .replace(containerViewId, fragment)
            .commitAllowingStateLoss()
}

fun ClosedRange<Int>.random() =
        Random().nextInt(endInclusive - start) + start

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun ViewGroup.inflate(layoutRes: Int): View =
        LayoutInflater.from(context).inflate(layoutRes, this, false)

fun View.setDrawableBackground(drawableResId: Int) {
    this.background = ContextCompat.getDrawable(this.context, drawableResId)
}

fun View.setColorBackground(colorResId: Int) {
    this.setBackgroundColor(ContextCompat.getColor(this.context, colorResId))
}

fun Activity.windowHeight(): Int {
    val display = this.windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.y
}

fun Activity.windowWidth(): Int {
    val display = this.windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}

fun Activity.hideSoftKeyboard(view: View?) {
    view?.let {
        val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun ImageView.loadUrl(url: String, requestOptions: RequestOptions, placeholder: Int = R.drawable.ic_placeholder, error: Int = R.drawable.ic_placeholder) {
    Glide.with(context)
            .load(url)
            .apply(requestOptions
                    .placeholder(placeholder)
                    .error(error))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
}

fun ImageView.loadCircularView(url: String? = null, bitmap: Bitmap? = null, placeholder: Int = R.drawable.ic_rounded_placeholder, error: Int = R.drawable.ic_rounded_placeholder) {
    val res = url ?: bitmap
    Glide.with(this.context)
            .load(res)
            .apply(RequestOptions.circleCropTransform()
                    .placeholder(placeholder)
                    .error(error))
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(RequestOptions.skipMemoryCacheOf(true))
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .into(this)
}

fun ImageView.loadBarcode(string: String, barcodeFormat: BarcodeFormat, width: Int = 250, height: Int = 100) {
    val multiFormatWriter = MultiFormatWriter()
    try {
        val bitMatrix = multiFormatWriter.encode(string, barcodeFormat, width, height)
        val barcodeEncoder = BarcodeEncoder()
        val qrCode = barcodeEncoder.createBitmap(bitMatrix)
        this.setImageBitmap(qrCode)
    } catch (writerException: WriterException) {
        writerException.printStackTrace()
    }
}

fun Drawable.overrideColor(backgroundColor: Int) {
    when (this) {
        is GradientDrawable -> setColor(backgroundColor)
        is ShapeDrawable -> paint.color = backgroundColor
        is ColorDrawable -> color = backgroundColor
    }
}

fun ViewGroup.inflate(viewId: Int, attachRoot: Boolean = false): View? {
    return LayoutInflater.from(context).inflate(viewId, this, attachRoot)
}

fun View.snack(message: String, length: Int = Snackbar.LENGTH_INDEFINITE) {
    val snack = Snackbar.make(this, message, length)
    snack.setAction(android.R.string.ok, null)
    snack.show()
}

fun String.encrypt(): String {
    return CryptoUtils.getInstance().encryptOrNull(this) ?: ""
}

fun String.decrypt(): String =
        CryptoUtils.getInstance().decryptOrNull(this) ?: ""

fun String.encryptAsync(listener: CryptoUtilsCallback) {
    val encryption = CryptoUtils.getInstance()
    encryption.encryptAsync(this, object : Encryption.Callback {
        override fun onSuccess(p0: String?) {
            listener.onSuccess(p0 ?: "")
        }

        override fun onError(p0: Exception?) {
            listener.onError(Throwable(p0?.message.toString()))
        }
    })
}

fun String.decryptAsync(listener: CryptoUtilsCallback) {
    val decryption = CryptoUtils.getInstance()
    decryption.decryptAsync(this, object : Encryption.Callback {
        override fun onSuccess(p0: String?) {
            listener.onSuccess(p0 ?: "")
        }

        override fun onError(p0: Exception?) {
            listener.onError(Throwable(p0?.message.toString()))
        }
    })
}